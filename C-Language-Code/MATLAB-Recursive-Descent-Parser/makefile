CFLAGS := -Wall -Wextra -Wpedantic -Wfloat-equal -Wvla -std=c99 -Werror -O2

# You need to provide Exact/exact.c and Exact/specific.h
# All other files are mine including the general.c/.h files which
# are copied from ADTS/General

all: parse interp

check: check.c
	clang check.c -o check

parse: parse.c
	clang parse.c $(CFLAGS) -o parse

do: do.c
	clang interp.c $(CFLAGS) -o interp

test: parse interp
	./parse
	./interp

clean:
	rm parse interp
