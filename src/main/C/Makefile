CC = gcc
CFLAGS = -Wall -fPIC -c

all : client.o libTwisk.so

libTwisk.so : programmeC.o client.o
		$(CC) -shared $^ -o $@

client.o : client.c
		$(CC) $(CFLAGS) $<

clean:
	rm -f client.o