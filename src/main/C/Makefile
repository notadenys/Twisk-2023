CC = gcc
CFLAGS = -Wall
LIBS = -lTwisk

all : main client.o libTwisk.so

main : main.c libTwisk.so
		$(CC) $(CFLAGS) -L. $< -o $@ $(LIBS)

libTwisk.so : programmeC.o client.o
		$(CC) -shared $^ -o $@

client.o : client.c
		$(CC) $(CFLAGS) -fPIC -c $<

clean:
	rm -f client.o libTwisk.so main