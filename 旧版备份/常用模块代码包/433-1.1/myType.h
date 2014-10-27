#ifndef __MYTYPE_H__
#define __MYTYPE_H__
/*
 * some device define 
 */
// LED define 
#define LED0 0x00
#define LED1 0x01
#define LED2 0x02
#define LED3 0x03

//433 wireless define
#define _433_DEV0 0x10
#define _433_DEV1 0x12
#define _433_DEV2 0x13
#define _433_DEV3 0x14

//local board define
#define LOCAL 0xff


#define ON 1
#define OFF 0
	 
#define _BYTE unsigned char 
#define TYPE8 _BYTE 
#define uchar TYPE8 
/*
 * 类型
 */
#define M_LOAD 0x20
#define M_REGISTER 0x21
#define M_REFRESH 0x22
#define M_CONTROL 0x23
#define M_QUIT 0x28
#define NO_DEFINE 0x29
#define M_HELP 0x24

/*
 * device file define	
 */
#define LED_FILE "/dev/LED0"
#define _433_FILE1 "/dev/ttySAC1"
#define _433_FILE2 "/dev/ttySAC2"
#define _433_FILE3 "/dev/ttySAC3"


#endif
