#ifndef __SERVER__H__
#define __SERVER__H__

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <malloc.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/ioctl.h>
#include <stdarg.h>
#include <fcntl.h>
#include <fcntl.h>
#include <pthread.h>
#include "sockWindows.h"

#define BACKLOG 10
/*
 *全局变量
 */
struct sockaddr_in server_servaddr;
static int listenfd;

/*
 * 函数声明
 */
void server_socket_init(unsigned short);
void server_socket_close();
void server_socket_body(unsigned short);

/*
 * 线程函数声明
 */
static void * doit(int connfd);
void server_socket_body(unsigned short port){
	server_socket_init(port);
	//int *iptr;
	//thread_t apid;
	//socklen_t addrlen,len;
	int connfd;
	printf("监听\n");
	//时刻监听socket，若有新连接，则新建线程进行处理.
	connfd = accept(listenfd,(struct sockaddr*)NULL, NULL);
	//pthread_create(&apid,NULL,&doit,connfd);
	doit(connfd);
	close(connfd);
	server_socket_close();
}
static void * doit(int connfd){
	//unsigned char rec[100];
	TYPE8 rec[100];
	read(connfd, rec, 100);
	
	write(connfd,rec, 100);
	printf("Hello World!\n");			
	return 0;
}
void server_socket_init(unsigned short port){
	char buff[100];
		
	// 建立socket 		
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	
	// 设置socket协议类型
	bzero(&server_servaddr, sizeof(server_servaddr));
	server_servaddr.sin_family = AF_INET;			//TCP	
	server_servaddr.sin_addr.s_addr = htonl(INADDR_ANY);   //IP
	server_servaddr.sin_port = htons(port);
	
	// bind端口
	bind(listenfd, (struct sockaddr*)&server_servaddr, sizeof(server_servaddr));

	// 监听端口
	listen(listenfd, BACKLOG);
}

void server_socket_close(){
	close(listenfd);
}



#endif
