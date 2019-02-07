#include <stdio.h>
int main(){
	FILE *fp;
	char ch;
	fp = fopen("Error.txt","a+");
	if(fp == NULL){
		printf("File Could Not be Opened\n");
	}
	system("iwlist scan wlan0 2> Error.txt");
	ch = fgetc(fp);
	while(1){
		if(ch != EOF){
			system("sh GlowLED.sh");
			break;
		}
		ch = fgetc(fp);
	}
	return 0;
}
