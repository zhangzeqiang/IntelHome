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

/*
 * socket function
 */
void init_socket(unsigned short,char const *);
void close_socket();
void client_run();

struct sockaddr_in servaddr;
int sockfd;

int main(){
	char IP[] = "127.0.0.1";
	//char IP[] = "192.168.1.101";	
	unsigned short port = 30000;
//init socket
	init_socket(port, IP);

// thread-1 finish update


// thread-2 finish readStream


// thread-3 handle user input.
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
			write(sockfd, send, sizeof(TYPE8)*send[0]);
		}else if(info.type == M_QUIT){
			//quit 			
			close_socket();
			return 0;
		}else{
				
		}
	}
	return 0;
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
	close(sockfd);
	printf("client socket close...\n");
}
