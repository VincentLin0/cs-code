#include <stdio.h>
#include <stdlib.h>
#include<string.h>


int str_equal(char *arr,char *arr2,int size){
    int i;
    for( i=0;i<size;i++){
        if(arr[i]!=arr2[i])
            return 0;
    }
    return 1;
}



int main(int argc , char ** argv)
{
    char begin[5]="BEGIN";
    char set[3]="SET";
    char print[5]="PRINT";
    char ones[4]="ONES";
    char B_add[5]="B-ADD";
    char B_times[7]="B-TIMES";
    char LOOP[7]="LOOP";

    int  idx=0;
    int ceng,lie;
    char str_name[100][100];
    int  str_value[100];
    char  str_2wei_name[100];
    int   arr_2[100][100];

    char str1[100];
    FILE * fp;
    FILE * fp1;

    if (argc < 2)
    {
        printf("argc input error\n");
        return 0;
    }

    fp1 = fopen (argv[1], "r");//读取文件，只读

    if (fp1 == NULL)
    {
        printf("open file failed!\n");
        return 0;
    }

    int fenhao=0;
    while(fscanf(fp1, "%s ", str1)!=EOF){//从文件中按单词读取字符，空格为分界线

            if(str_equal(str1,"{",1)){//如果{ } 不匹配，则退出程序，粗略的代码检测
                fenhao+=1;
    //            printf("%d\n",fenhao);
            }else if(str_equal(str1,"}",1)){
                fenhao-=1;
    //            printf("%d\n",fenhao);
            }
    }

    if(fenhao!=0){
        printf("improper format\n");return 0;
    }
   return 0;
}