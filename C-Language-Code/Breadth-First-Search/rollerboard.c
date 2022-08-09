#include <string.h>
#include <stdio.h>
#include <math.h>
#define MAX_C 8
#define MAX_R 8
#define MAX_D 100000			// 宽度优先生成树的最大深度 
#define MAX_N 1000000			// 队列能容纳的最大元素个数 
#define MAX_S 10000000			// 遍历过的棋盘集合的最大个数 
#define MAX_W 100000000			// 遍历过的棋盘所对应的十进制长长整型的最大范围 
typedef unsigned long long ull;
// 行数，列数 
int R, C;
// 初始棋盘  
ull N = -1;
typedef ull ElemType;
// 队列 
typedef struct Queue {
	ElemType element[MAX_N];	// 队列元素 
	int front, rear;			// 队头下标、队尾下标  
} Queue;
// 初始化队列 
void initialize_queue(Queue *q) {
	q->front = q->rear = 0;
}
// 队空 
int empty(Queue *q) {	
	return q->front == q->rear;
}
// 队满  
int full(Queue *q) {
	return (q->rear + 1) % MAX_N == q->front;
}
// 队列中元素数量  
int num_of_element(Queue *q) {
	return (q->rear - q->front + MAX_N) % MAX_N;
}
// 入队 
int push(Queue *q, ElemType e) {
	if (full(q)) {
		return 0;
	} else {
		q->element[q->rear] = e;
		q->rear = (q->rear + 1) % MAX_N;
		return 1;
	}
}
// 出队  
int pop(Queue *q, ElemType *e) {
	if (empty(q)) {
		return 0;
	} else {
		*e = q->element[q->front];
		q->front = (q->front + 1) % MAX_N;
		return 1;
	}
}
// 反转字符串  
void reverse_char(char s[]) {
	for (int i = 0; i < strlen(s) / 2; i++) {
		char t = s[i];
		s[i] = s[strlen(s) - 1 - i];
		s[strlen(s) - 1 - i] = t;
	}
}
// 将以二进制字符串存储的棋盘转换为十进制长长整型  
ull to_num(char s[]) {
	ull res = 0;
	reverse_char(s);
	for (int i = 0; i < strlen(s); i++) res += pow(2, i) * (s[i] - '0');
	return res;
}
// 取出棋盘n的第x行第y列上的元素 
ull B(ull n, int x, int y) {
	return (n >> (R * C - C * x - y - 1)) & 1;
}
// 将棋盘n的第x行第y列上的元素赋值为d  
void cB(ull *n, int x, int y, int d) {
	*n = d ? *n | (1 << (R * C - C * x - y - 1)) : *n & ~(1 << (R * C - C * x - y - 1));
}
// 结果棋盘（即第0行全1，其余行全0的棋盘 ）对应的十进制长长整型 
ull rB() {
	ull r = 0;
	for (int j = 0; j < C; j++) cB(&r, 0, j, 1);
	return r;
}
// 将棋盘n的第j列上滚一位后得到的棋盘对应的十进制长长整型 
ull cu(ull n, int j) {
	int t = B(n, 0, j);
	for (int i = 1; i <= R - 1; i++) cB(&n, i - 1, j, B(n, i, j));
	cB(&n, R - 1, j, t);
}
// 将棋盘n的第j列下滚一位后得到的棋盘对应的十进制长长整型 
ull cd(ull n, int j) {
	int t = B(n, R - 1, j);
	for (int i = R - 1; i >= 1; i--) cB(&n, i, j, B(n, i - 1, j));
	cB(&n, 0, j, t);
}
// 将棋盘n的第i行左滚一位后得到的棋盘对应的十进制长长整型 
ull rl(ull n, int i) {
	int t = B(n, i, 0);
	for (int j = 1; j <= C - 1; j++) cB(&n, i, j - 1, B(n, i, j));
	cB(&n, i, C - 1, t);
}
// 将棋盘n的第i行右滚一位后得到的棋盘对应的十进制长长整型 
ull rr(ull n, int i) {
	int t = B(n, i, C - 1);
	for (int j = C - 1; j >= 1; j--) cB(&n, i, j, B(n, i, j - 1));
	cB(&n, i, 0, t);
}
// 输入数据（行数、列数、初始棋盘） 
void input() {
	FILE *fp = fopen("C:\\Users\\Fortis931\\Desktop\\s.txt", "r+");
	if (!fp) {
		printf("cannot open file!");
		return;
	} 
	char t[MAX_R][MAX_C];
	fscanf(fp, "%d%d", &R, &C);
	for (int i = 0; i < R; i++) fscanf(fp, "%s", t[i]);
	for (int i = 1; i < R; i++) strcat(t[0], t[i]);
	N = to_num(t[0]);
}
// 遍历过的棋盘集合 
ull set[MAX_S];
// 遍历过的棋盘集合的长度 
int len_set = 0;
// 遍历过程中棋盘的上一个棋盘，即棋盘i由棋盘pre[i]生成，注意如果R*C较大需要扩大MAX_W  
ull pre[MAX_W];
// 棋盘n在遍历过的棋盘集合中的位置，如果棋盘n未被遍历，则返回-1  
int loc(ull n) {
	for (int i = 0; i < len_set; i++) if (set[i] == n) return i;
	return -1;
}
// 反转十进制长长整型数组  
void reverse_ull(ull r[], int lr) {
	for (int i = 0; i < lr / 2; i++) {
		ull t = r[i];
		r[i] = r[lr - 1 - i];
		r[lr - 1 - i] = t;
	}
}
// 输出以棋盘n为最终棋盘的变化过程，即先输出初始棋盘，然后输出初始棋盘的生成棋盘，...，最后输出最终棋盘  
void output(ull n) {
	ull r[MAX_D];
	int lr = 0;
	while (pre[n] != -1) {
		r[lr++] = n;
		n = pre[n];
	}
	r[lr++] = N;
	reverse_ull(r, lr); 
	for (int k = 0; k < lr; k++) {
		printf("%d:\n", k);
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				printf("%d", B(r[k], i, j));
			}
			printf("\n");
		}
		printf("\n");
	}
}
// 宽度优先搜索过程中用到的队列  
Queue q;
// 宽度优先搜索  
void solve() {
	if (N == -1) return;
	ull rN = rB(), front, tN;			// 计算结果棋盘rN，定义当前遍历到的棋盘front及滚动后的棋盘tN 
	initialize_queue(&q);				// 初始化队列  
	memset(pre, -1, sizeof(pre));		// 初始化遍历过程中棋盘的上一个棋盘，即一开始所有棋盘都没有上一个棋盘 
	push(&q, N);						// 初始棋盘入队 
	set[len_set++] = N;					// 将初始棋盘归入遍历过的棋盘集合  
	while (!empty(&q)) {				// 只要队列不空 
		pop(&q, &front);				// 队头元素出队，并赋值给front，front即为当前遍历到的棋盘  
		if (front == rN) {				// 如果当前遍历到的棋盘就是结果棋盘  
			output(rN);					// 输出棋盘的变化过程 
			break;						// 跳出循环 
		}
		for (int i = 0; i < R; i++) {	// 对于每一行  
			tN = rl(front, i);			// 计算当前遍历到的棋盘第i行左滚一位后的棋盘  
			if (loc(tN) == -1) {		// 如果该棋盘未被遍历过  
				push(&q, tN);			// 该棋盘入队 
				set[len_set++] = tN;	// 将该棋盘归入遍历过的棋盘集合 
				pre[tN] = front;		// 记录该棋盘的上一个棋盘  
			}
			tN = rr(front, i);			// 计算当前遍历到的棋盘第i行右滚一位后的棋盘 
			if (loc(tN) == -1) {		// 如果该棋盘未被遍历过  
				push(&q, tN);			// 该棋盘入队 
				set[len_set++] = tN;	// 将该棋盘归入遍历过的棋盘集合 
				pre[tN] = front;		// 记录该棋盘的上一个棋盘  
			}
		}
		for (int j = 0; j < C; j++) {	// 对于每一列  
			tN = cu(front, j);			// 计算当前遍历到的棋盘第j列上滚一位后的棋盘 
			if (loc(tN) == -1) {		// 如果该棋盘未被遍历过  
				push(&q, tN);			// 该棋盘入队 
				set[len_set++] = tN;	// 将该棋盘归入遍历过的棋盘集合 
				pre[tN] = front;		// 记录该棋盘的上一个棋盘  
			}
			tN = cd(front, j);			// 计算当前遍历到的棋盘第j列下滚一位后的棋盘 
			if (loc(tN) == -1) {		// 如果该棋盘未被遍历过  
				push(&q, tN);			// 该棋盘入队 
				set[len_set++] = tN;	// 将该棋盘归入遍历过的棋盘集合 
				pre[tN] = front;		// 记录该棋盘的上一个棋盘  
			}
		}
	}
}
int main() {
	input();
	solve();
	return 0;
}
/*

3 3
000
001
011

4 4
0000
1001
1001
0000

5 4
0000
1100
1000
0100
0000

*/
