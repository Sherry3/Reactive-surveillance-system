#include <stdio.h>
int main(int argc, char **args){
        FILE *fd;
	fd = fopen("InetAddr.txt","w");
        system("ifconfig eth0 | grep 'inet addr' | cut -d: -f2 | awk '{print $1}' >> InetAddr.txt");
	system("ifconfig wlan0 | grep 'inet addr' | cut -d: -f2 | awk '{print $1}' >> InetAddr.txt");
	return 0;
}
