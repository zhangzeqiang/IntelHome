#ifndef __PROTOCOL_H__
#define __PROTOCOL_H__

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<unistd.h>
#include<termios.h>
#include<string.h>
#include"myType.h"

struct info_node{
	TYPE8 dev_from;		//表示信息源设备地址
	TYPE8 dev_to;		//表示信息目的设备地址
	TYPE8 dev_state;	//表示目的设备其他信息
};
struct dev_node{		
	TYPE8 devno;		//表示设备号
	TYPE8 state;		//表示设备状态
};

void itn(const TYPE8 *i, struct info_node *node);
void nti(const struct info_node *node, TYPE8 *i);

void itn(const TYPE8 *i, struct info_node *node){
	node->dev_from = i[0];
	node->dev_to = i[1];
	node->dev_state = i[2];
}
void nti(const struct info_node *node, TYPE8 *i){
	i[0] = node->dev_from;
	i[1] = node->dev_to;
	i[2] = node->dev_state;
}

#endif
