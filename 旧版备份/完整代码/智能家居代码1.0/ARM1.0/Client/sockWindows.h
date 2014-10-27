#ifndef __SOCKWINDOWS__H__
#define __SOCKWINDOWS__H__

#include <stdio.h>
#include "myType.h"

struct my_user_info{
	TYPE8 type;
	TYPE8 id;
	TYPE8 passwd;
};
int mainWindows(struct my_user_info* info);
int loadWindows(struct my_user_info* info);

int mainWindows(struct my_user_info* info){
	char ch;
	char introduce[] = "Input a word to enter different mode windows.\n\
1.Quit 2.Load 3.Register 4.Control Other number to get help!\n";
	printf("%s", introduce);
	while (ch = getchar()){
		switch (ch){
		//case 'Q':
		case '1':{
			info->type = M_QUIT;
			goto out;
		}break;
		//case 'L':
		case '2':{
			info->type = M_LOAD;
			loadWindows(info);
			goto out;
		}break;
		//case 'R':
		case '3':{
			info->type = NO_DEFINE;
			errorWindows();
		}break;
		/*case 'F':{
		}break;*/
		//case 'C':
		case '4':{
			info->type = NO_DEFINE;
			errorWindows();
		}break;
		default :{
			info->type = M_HELP;
			printf("%s", introduce);
		}break;
		}
	//	printf("%s\n",send);
	//	fflush(stdin);		gcc下fflush无效
		while ((ch=getchar()) != '\n' && ch != EOF);		
	}
out:
	while ((ch=getchar()) != '\n' && ch != EOF);
	return 0;
}
int loadWindows(struct my_user_info *info){
	char statement[] = "you are in load mode now.Please input id and passwd. ";
	printf("%s\n", statement);
		
	//input id
	//int id = 0;
	printf("input id:");
	scanf("%d", (int *)&(info->id));
	//input passwd
	//int passwd = 0;
	printf("input passwd:");
	scanf("%d", (int *)&(info->passwd));
	return 0;
}
int registerWindows(){
	return 0;
}
int errorWindows(){
	printf("no define cmd.\n");
	return 0;
} 
#endif
