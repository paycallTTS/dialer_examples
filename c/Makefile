SHELL:=bash

CLANGPATH="/usr/bin/clang"
GCCPATH="/usr/bin/gcc"
CC=${CLANGPATH}

.phony: clean

build:
	${CC} -v -Wall -Werror -g -ggdb -glldb `pkg-config --cflags --libs uuid` `pkg-config --cflags --libs pkg-config --libs libcurl` simple_dialer.c -o simple_dialer

clean:
	rm simple_dialer

build_and_run: build
	./simple_dialer
