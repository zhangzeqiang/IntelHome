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
#include "sockWindows.h"
#include "server.h"

#define MAXBIT 100

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

int main(){
	char IP[] = "192.168.1.101";
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
		mainWindows(&info);
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
			if (write(sockfd, send, MAXBIT) <= 0){
				close_socket();
				return 0;
			}
		}else if(info.type == M_QUIT){
			//quit 			
			close_socket();
			return 0;
		}else{
				
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
	TYPE8 send[MAXBIT];
	send[0] = 0x0a;
	send[1] = M_REFRESH;
	send[2] = 0x01;
	send[3] = 0x10;
	send[4] = 1;	//state
	send[5] = 2;	//dev_num
	send[6] = 3;	//devno1
	send[7] = 0;	//devState1
	send[8] = 11;	//devno2
	send[9] = 1;	//devState2
	if (write(sockfd, send, MAXBIT) <= 0){
        	close_socket();
        	return 0;
        }
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
				printf("len=%d,type=%d,ifsuccess=%d\n",rec[0],rec[1],rec[2]);
			}break;
			case M_REFRESH:{
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
