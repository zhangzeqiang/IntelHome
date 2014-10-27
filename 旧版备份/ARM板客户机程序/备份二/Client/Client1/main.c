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
#include "sockWindows.h"

extern int mainWindows(struct my_user_info*);		//struct user_info 不能用()括起来，不然会报错
void init_socket(unsigned short,char const *);
void close_socket();
void client_run();

//常用操作
void s_load(struct my_user_info*);
void *readInputStream();
struct sockaddr_in servaddr;
int sockfd;

//处理函数
void h_load(TYPE8*, size_t);

int main(int argc, char **argv){
	char IP[] = "127.0.0.1";
	//char IP[] = "192.168.1.101";	
	unsigned short port = 30000;
//启动socket
	init_socket(port,IP);

//client_run
	client_run();

//关闭socket
	close_socket();
	return 0;
}

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
void client_run(){
	
pthread_t tid;

//线程负责随时读取socket数据
	pthread_create(&tid, NULL, &readInputStream, NULL);



//开发板隔一段时间刷新一次,由新线程负责刷新


//获取登录注册信息,接受用户输入
	struct my_user_info *info;
	info = malloc(sizeof(struct my_user_info));
	info->type = M_HELP;
	while(1){
		mainWindows(info);
		if (info->type == M_QUIT){
			break;
		}
	//	printf("type=%i", info->type);
	//	printf("id=%i,passwd=%d\n", info->id, info->passwd);
		//获取用户输入消息,并新建一个线程读消息进行处理
		switch (info->type){
			case M_LOAD:{
				s_load(info);		
			}break;
			case M_REFRESH:{
				printf("注册成功:id=%i,passwd=%i\n", info->id, info->passwd);
			}break;
			case M_QUIT:{
				goto out;
			}break;
		}
	}
out:
	free(info);

}
void* readInputStream(){
	pthread_detach(pthread_self());
	//printf("hello World!");
	TYPE8 rec[100];
	while (read(sockfd, rec, sizeof(TYPE8)*100) > 0){
		//成功读取数据	
		switch (rec[1]){
			case M_LOAD:{
				h_load(rec, sizeof(TYPE8)*100);
			}break;
			case M_REGISTER:{

			}break;
			case M_REFRESH:{

			}break;
			case M_CONTROL:{

			}break;
			default:{

			}break;
		}	
	}

	//socket断开连接
	close_socket();
	exit(0);	
}
void h_load(TYPE8 rec[], size_t len){
		
	/*if (read(sockfd, rec, sizeof(TYPE8)*4) < 0){
		printf("socket 连接断开!\n");
		exit(0);
	}*/
	printf("%d\n", rec[2]);
	if (rec[2]){
		printf("%d", rec[2]);
		printf("登录成功\n");
	}else{
		printf("登录失败\n");
	}

}
void s_load(struct my_user_info* info){
	printf("id=%i,passwd=%i\n", info->id, info->passwd);
	TYPE8 send[6];
	send[0] = 4;
	send[1] = 0x20;
	send[2] = (TYPE8)info->id;
	send[3] = (TYPE8)info->passwd;
	write(sockfd, send, sizeof(TYPE8)*send[0]);
}
void close_socket(){
	close(sockfd);
	printf("client socket close...\n");
}
