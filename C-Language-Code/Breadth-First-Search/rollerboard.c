#include <string.h>
#include <stdio.h>
#include <math.h>
#define MAX_C 8
#define MAX_R 8
#define MAX_D 100000			// ��������������������� 
#define MAX_N 1000000			// ���������ɵ����Ԫ�ظ��� 
#define MAX_S 10000000			// �����������̼��ϵ������� 
#define MAX_W 100000000			// ����������������Ӧ��ʮ���Ƴ������͵����Χ 
typedef unsigned long long ull;
// ���������� 
int R, C;
// ��ʼ����  
ull N = -1;
typedef ull ElemType;
// ���� 
typedef struct Queue {
	ElemType element[MAX_N];	// ����Ԫ�� 
	int front, rear;			// ��ͷ�±ꡢ��β�±�  
} Queue;
// ��ʼ������ 
void initialize_queue(Queue *q) {
	q->front = q->rear = 0;
}
// �ӿ� 
int empty(Queue *q) {	
	return q->front == q->rear;
}
// ����  
int full(Queue *q) {
	return (q->rear + 1) % MAX_N == q->front;
}
// ������Ԫ������  
int num_of_element(Queue *q) {
	return (q->rear - q->front + MAX_N) % MAX_N;
}
// ��� 
int push(Queue *q, ElemType e) {
	if (full(q)) {
		return 0;
	} else {
		q->element[q->rear] = e;
		q->rear = (q->rear + 1) % MAX_N;
		return 1;
	}
}
// ����  
int pop(Queue *q, ElemType *e) {
	if (empty(q)) {
		return 0;
	} else {
		*e = q->element[q->front];
		q->front = (q->front + 1) % MAX_N;
		return 1;
	}
}
// ��ת�ַ���  
void reverse_char(char s[]) {
	for (int i = 0; i < strlen(s) / 2; i++) {
		char t = s[i];
		s[i] = s[strlen(s) - 1 - i];
		s[strlen(s) - 1 - i] = t;
	}
}
// ���Զ������ַ����洢������ת��Ϊʮ���Ƴ�������  
ull to_num(char s[]) {
	ull res = 0;
	reverse_char(s);
	for (int i = 0; i < strlen(s); i++) res += pow(2, i) * (s[i] - '0');
	return res;
}
// ȡ������n�ĵ�x�е�y���ϵ�Ԫ�� 
ull B(ull n, int x, int y) {
	return (n >> (R * C - C * x - y - 1)) & 1;
}
// ������n�ĵ�x�е�y���ϵ�Ԫ�ظ�ֵΪd  
void cB(ull *n, int x, int y, int d) {
	*n = d ? *n | (1 << (R * C - C * x - y - 1)) : *n & ~(1 << (R * C - C * x - y - 1));
}
// ������̣�����0��ȫ1��������ȫ0������ ����Ӧ��ʮ���Ƴ������� 
ull rB() {
	ull r = 0;
	for (int j = 0; j < C; j++) cB(&r, 0, j, 1);
	return r;
}
// ������n�ĵ�j���Ϲ�һλ��õ������̶�Ӧ��ʮ���Ƴ������� 
ull cu(ull n, int j) {
	int t = B(n, 0, j);
	for (int i = 1; i <= R - 1; i++) cB(&n, i - 1, j, B(n, i, j));
	cB(&n, R - 1, j, t);
}
// ������n�ĵ�j���¹�һλ��õ������̶�Ӧ��ʮ���Ƴ������� 
ull cd(ull n, int j) {
	int t = B(n, R - 1, j);
	for (int i = R - 1; i >= 1; i--) cB(&n, i, j, B(n, i - 1, j));
	cB(&n, 0, j, t);
}
// ������n�ĵ�i�����һλ��õ������̶�Ӧ��ʮ���Ƴ������� 
ull rl(ull n, int i) {
	int t = B(n, i, 0);
	for (int j = 1; j <= C - 1; j++) cB(&n, i, j - 1, B(n, i, j));
	cB(&n, i, C - 1, t);
}
// ������n�ĵ�i���ҹ�һλ��õ������̶�Ӧ��ʮ���Ƴ������� 
ull rr(ull n, int i) {
	int t = B(n, i, C - 1);
	for (int j = C - 1; j >= 1; j--) cB(&n, i, j, B(n, i, j - 1));
	cB(&n, i, 0, t);
}
// �������ݣ���������������ʼ���̣� 
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
// �����������̼��� 
ull set[MAX_S];
// �����������̼��ϵĳ��� 
int len_set = 0;
// �������������̵���һ�����̣�������i������pre[i]���ɣ�ע�����R*C�ϴ���Ҫ����MAX_W  
ull pre[MAX_W];
// ����n�ڱ����������̼����е�λ�ã��������nδ���������򷵻�-1  
int loc(ull n) {
	for (int i = 0; i < len_set; i++) if (set[i] == n) return i;
	return -1;
}
// ��תʮ���Ƴ�����������  
void reverse_ull(ull r[], int lr) {
	for (int i = 0; i < lr / 2; i++) {
		ull t = r[i];
		r[i] = r[lr - 1 - i];
		r[lr - 1 - i] = t;
	}
}
// ���������nΪ�������̵ı仯���̣����������ʼ���̣�Ȼ�������ʼ���̵��������̣�...����������������  
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
// ������������������õ��Ķ���  
Queue q;
// �����������  
void solve() {
	if (N == -1) return;
	ull rN = rB(), front, tN;			// ����������rN�����嵱ǰ������������front�������������tN 
	initialize_queue(&q);				// ��ʼ������  
	memset(pre, -1, sizeof(pre));		// ��ʼ���������������̵���һ�����̣���һ��ʼ�������̶�û����һ������ 
	push(&q, N);						// ��ʼ������� 
	set[len_set++] = N;					// ����ʼ���̹�������������̼���  
	while (!empty(&q)) {				// ֻҪ���в��� 
		pop(&q, &front);				// ��ͷԪ�س��ӣ�����ֵ��front��front��Ϊ��ǰ������������  
		if (front == rN) {				// �����ǰ�����������̾��ǽ������  
			output(rN);					// ������̵ı仯���� 
			break;						// ����ѭ�� 
		}
		for (int i = 0; i < R; i++) {	// ����ÿһ��  
			tN = rl(front, i);			// ���㵱ǰ�����������̵�i�����һλ�������  
			if (loc(tN) == -1) {		// ���������δ��������  
				push(&q, tN);			// ��������� 
				set[len_set++] = tN;	// �������̹�������������̼��� 
				pre[tN] = front;		// ��¼�����̵���һ������  
			}
			tN = rr(front, i);			// ���㵱ǰ�����������̵�i���ҹ�һλ������� 
			if (loc(tN) == -1) {		// ���������δ��������  
				push(&q, tN);			// ��������� 
				set[len_set++] = tN;	// �������̹�������������̼��� 
				pre[tN] = front;		// ��¼�����̵���һ������  
			}
		}
		for (int j = 0; j < C; j++) {	// ����ÿһ��  
			tN = cu(front, j);			// ���㵱ǰ�����������̵�j���Ϲ�һλ������� 
			if (loc(tN) == -1) {		// ���������δ��������  
				push(&q, tN);			// ��������� 
				set[len_set++] = tN;	// �������̹�������������̼��� 
				pre[tN] = front;		// ��¼�����̵���һ������  
			}
			tN = cd(front, j);			// ���㵱ǰ�����������̵�j���¹�һλ������� 
			if (loc(tN) == -1) {		// ���������δ��������  
				push(&q, tN);			// ��������� 
				set[len_set++] = tN;	// �������̹�������������̼��� 
				pre[tN] = front;		// ��¼�����̵���һ������  
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
