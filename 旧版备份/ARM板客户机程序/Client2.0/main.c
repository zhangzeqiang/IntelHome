#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <pthread.h>
#include <malloc.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/ioctl.h>
#include <stdarg.h>
#include <fcntl.h>
#include <fcntl.h>
#include <pthread.h>
#include <sys/select.h>
#include <sys/time.h>
#include "include/sockWindows.h"
#include "include/server.h"
#include "include/myType.h"
#include "include/protocol.h"
#include "module/module_led.h"
#include "module/module_433.h"

#define MAXBIT 100
#define REFRESH_TIME_SEC 5

/*
 * socket function
 */
void init_socket(unsigned short,char const *);
void close_socket();
void client_run();

/*
 * some function
 */
//static int s_control();

/*
 * thread func
 */
static void* readStream();
static void* refreshInfo();
static void* otherConnect();

struct sockaddr_in servaddr;
int sockfd;
struct my_user_info Info={M_LOAD,1,16};
int ifLoad = LOADING;

int main(){
	char IP[] = "192.168.1.100";
	//char IP[] = "192.168.1.101";	
	unsigned short port = 30030;
//init socket
	init_socket(port, IP);

// thread-1 finish update
	pthread_t fpid;
	pthread_create(&fpid, NULL, refreshInfo, NULL);

// thread-2 finish readStream
	pthread_t rpid;
	pthread_create(&rpid, NULL, readStream, NULL);

// thread-3 finish catch server's connect.(等待服务器或手机的主动连接申请)
	pthread_t spid;
	pthread_create(&spid, NULL, otherConnect, NULL);

// thread-4 handle user input.
	struct my_user_info info;
	while (1){
		mainWindows(&info);			//获取用户输入
		//printf("%d\n", info.type);
		//printf("%d\n", (TYPE8)info.id);
		//printf("%d\n", (TYPE8)info.passwd);
		if (info.type == M_LOAD){
			//load...
			TYPE8 send[6];
			send[0] = 4;
			send[1] = 0x20;
			send[2] = info.id;
			send[3] = info.passwd;
			
			Info.id = info.id;
			Info.passwd = info.passwd;
			Info.type = info.type;

			if (write(sockfd, send, MAXBIT) <= 0){
				close_socket();
				return 0;
			}
		}else if(info.type == M_QUIT){
			//quit 			
			close_socket();
			return 0;
		}else{
			//add operation ... --- TODO	
		}
	}
	return 0;
}

void * otherConnect(){
	//unsigned short port = (*(unsigned short*)arg);
	//included in a new header...	
	unsigned short port = 30031;
	server_socket_body(port);	
	return 0;
}

void * refreshInfo(){
	int size = 0;
	int i = 0, led_fd;
	uchar dev_num;
	uchar buff[2];
	while (1){
		if (Info.type == M_LOAD && ifLoad == LOADING){		//只有登录成功才会刷新
			struct timeval t;
			t.tv_sec = REFRESH_TIME_SEC;
			t.tv_usec = 0;
			select(0, NULL, NULL, NULL, &t);		//定时器
		
			TYPE8 send[MAXBIT];
			//send[0] = 0x0a;
			send[1] = M_REFRESH;
			send[2] = Info.id;
			send[3] = Info.passwd;
			send[4] = 1;	//state
			size = 5;
			
			//-------------------------------------------
			//获取dev_num的值
			//update dev_num --- TODO
			dev_num = 4;			//4led device
			// -------------------------------------------

			size = size+dev_num*2+1;
			send[5] = dev_num;	//dev_num
			send[0] = size;
			
			//-------------------------------------------	
			//open device --- TODO
			led_fd = open(LED_FILE, O_RDWR);
	
			if (led_fd < 0){
			//------------------------------------------
				perror("open device leds error!\n");
				//exit(1);
				send[0] = 3;
				send[2] = 0;
				if (write(sockfd, send, MAXBIT) <= 0){
	       		 		close_socket();
					return 0;
	       			}
				//close device
				goto close_device;
			}

			//--------------------------------------------
			//add device refresh func...------TODO
			//1.add led refresh func
			struct dev_node *dev = malloc(4*sizeof(struct dev_node));
			if (readInfo_led(led_fd, dev, 4) > 0){
				for (i=0;i<4;i++){
					send[i*2+7] = dev[i].state;
					send[i*2+6] = dev[i].devno;
				}
			}		
			free(dev);
	
			/*
			read(led_fd, buff, 1);
			for (i=0;i<4;i++){
				send[i*2+7] = ((1<<i) & buff[0])>>i;		//state	
				send[i*2+6] = i;				//devno
			}
			*/
			
			//-------------------------------------------
	
			if (write(sockfd, send, MAXBIT) <= 0){
	       		 	goto close_socket;
	       		 }
close_device:
			//--------------------------------------------
			//close device --- TODO
			close(led_fd);
			
			//--------------------------------------------
		}
	}
close_socket:
	close_socket();
}

void *readStream(){
	TYPE8 rec[MAXBIT];	
	
	while (1){
		if (read(sockfd, rec, sizeof(TYPE8)*MAXBIT) <= 0){
			//socket disconnect...
			//close_socket();
			printf("server disconnect!\n");
			return 0;
		}
		switch (rec[1]){
			case M_LOAD:{
				printf("REFRESH:\n");
				printf("len=%d,type=%d,ifsuccess=%d\n",rec[0],rec[1],rec[2]);
				ifLoad = rec[2];		//表示已经登录成功
			}break;
			case M_REFRESH:{
				printf("REFRESH:\n");
				printf("len=%d,type=%d,ifsuccess=%d\n",rec[0],rec[1],rec[2]);	
			}break;
			default:{

			}break;
		}
		
	}
}

/*
 * init socket
 */
void init_socket(unsigned short port, char const *IP){
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
		printf("socket error: %s(errno: %d)\n",strerror(errno),errno);
		exit(0);
	}
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;				//TCP type
	servaddr.sin_port = htons(port);			//server port
	inet_pton(AF_INET, IP, &servaddr.sin_addr);	//server IP
	printf("client socket init...\n");
	if (connect(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr)) == -1){
		printf("connect error: %s(errno: %d)\n",strerror(errno),errno);
		exit(0);
	}else{
		printf("client connect!\n");
	}
}

/*
 * close socket
 */ 
void close_socket(){
	//close(sockfd);
	shutdown(sockfd, SHUT_WR);
	printf("client socket close...\n");
}
