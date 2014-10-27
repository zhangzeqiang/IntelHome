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

#define BACKLOG 10
void init_socket(unsigned short);
void exit_socket();
void server_body();

int listenfd, connfd;

int main(){
	unsigned short port = 30000;	
	
       	// accept等待连接

	// 关闭socket
	init_socket(port);

//	printf("socket Server!\n");
	server_body();
	
	exit_socket();
	return 0;
}
void server_body(){
	char buff[100];
	int n;
	printf("服务器正在监听\n");
	connfd = accept(listenfd, (struct sockaddr*)NULL, NULL);
	printf("建立新连接\n");
	//write(connfd, buff, strlen(buff));
	while((n=read(connfd, buff, 100)) > 0){
		buff[n] = 0;
		printf("%s\n", buff);
	}
	close(connfd);
	printf("断开连接\n");
}

void init_socket(unsigned short port){
	struct sockaddr_in servaddr;
	char buff[100];
		
	// 建立socket 		
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	
	// 设置socket协议类型
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;			//TCP	
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);   //IP
	servaddr.sin_port = htons(port);
	
	// bind端口
	bind(listenfd, (struct sockaddr*)&servaddr, sizeof(servaddr));

	// 监听端口
	listen(listenfd, BACKLOG);
	printf("socket init...\n");
}
void exit_socket(){
	close(listenfd);
	printf("socket close...\n");
}
