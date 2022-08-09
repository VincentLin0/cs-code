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



int main(int argc, char ** argv)
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

    if (argc < 2)
    {
        printf("argc input error\n");
        return 0;
    }

    fp = fopen (argv[1], "r");
        if (fp == NULL)
    {
        printf("open file failed!\n");
        return 0;
    }

    int beg=0;
     while(fscanf(fp, "%s ", str1)!=EOF){
        if(beg==0){
            if(str_equal(str1,begin,sizeof(begin))){//读到begin才开始运行程序，之前的# 注释内容不计入总程序
                beg=1;
            }
            else{
                continue;
        }
    }



        if(strcmp(str1,LOOP)==0){//loop 循环模块
            char tmp_var_name[50];
                char str_tmp2[50];
                char str_tmp3[50];
            int tmp_var_value;


            fscanf(fp, "%s ", tmp_var_name);//$I

            fscanf(fp, "%s ", str1);

            tmp_var_value =  atoi(str1);//10   //将字符串转为 int 类型

            int ff=-1;
            int  i;
            for(i=0;i<idx;i++){
                if(str_equal(str_name[i],tmp_var_name,strlen(tmp_var_name))){  //判断字符串是否相等,如果字符串相等，则记录该字符串的id值
                    ff=i;
                }
            }

            int wai_idx;
            if(ff==-1){
                str_value[idx]=tmp_var_value;

                strcpy(str_name[idx],tmp_var_name);
                wai_idx=idx;
                idx++;
            }else{
                str_value[ff]=tmp_var_value;
                wai_idx=ff;
            }
//            printf("%d\n",idx);

            fscanf(fp, "%s ", str1);//{
            fscanf(fp, "%s ", str1);//set

            if(str_equal(str1,LOOP,strlen(LOOP))){//二层循环进入处
                                fscanf(fp, "%s ", tmp_var_name);//$I 读取变量名

                            fscanf(fp, "%s ", str1);

                            tmp_var_value =  atoi(str1);//10  读取变量值

                            int ff=-1;
                            int  i;
                            for(i=0;i<idx;i++){
                                if(str_equal(str_name[i],tmp_var_name,strlen(tmp_var_name))){
                                    ff=i;
                                }
                            }
                            if(ff==-1){
                                str_value[idx]=tmp_var_value;

                                strcpy(str_name[idx],tmp_var_name);
                                idx++;
                            }else{
                                str_value[ff]=tmp_var_value;
                            }
                //            printf("%d\n",idx);

                            fscanf(fp, "%s ", str1);//{     无用{字符
                            fscanf(fp, "%s ", str1);//set   无用set字符
                            fscanf(fp, "%s ", str1);//$F            读取变量值

                    int t=-1;

                    for(i=0;i<idx;i++){
                        if(str_equal(str_name[i],str1,strlen(str1))){  //  记录变量名 对应变量的id值
                            t=i;
                        }
                    }
//                    printf("%d\n",t);

                    int cur_idx;
                    if(t==-1)
                    {strcpy(str_name[idx],str1);  // 把变量名记录到新的id值里，并将cur_idx更新，方便接下来的运用
                        cur_idx=idx;
                    }

                    if(t!=-1){
                        cur_idx=t;
                    }

                    fscanf(fp, "%s ", str_tmp2);//  kongge
                    fscanf(fp, "%s ", str_tmp2);//  val

                    if(str_tmp2[0]!='$'){//如果该输入处不是变量，则直接赋值，如果是变量则，进入else判断
                            str_value[cur_idx]=atoi(str_tmp2);

                    }
                    else{//loop内循环对代码的判断  模块与其他相同
                        int a_idx ;
                        int b_idx ;
                        fscanf(fp, "%s ", str_tmp3);//  val
                        int f=-1;
                        if(str_tmp3[0]!='$'){
                                int tmp = atoi(str_tmp3);
//                                printf("%d\n",tmp);
                            int i;
                            for( i=0;i<idx;i++){
//                                printf("i: %d  %s\n",i,str_name[i]);
                                if(str_equal(str_tmp2,str_name[i],strlen(str_tmp2))){
                                    a_idx=i;f+=1;break;
                                }
                            }
                            fscanf(fp, "%s ", str_tmp3);// operation
//                           printf("%s",str_tmp3);
                            fscanf(fp, "%s ", str_tmp2);//;  这里和上面同理
                            fscanf(fp, "%s ", str_tmp2);//
                            int op=-1;



                        if(str_equal(str_tmp2,print,strlen(str_tmp2))){
                            op=1;
                            fscanf(fp, "%s ", str_tmp2);//获取$F
                            int  z;
                            for(z=0;z<idx;z++){
                                if(strcmp(str_tmp2,str_name[z])==0){
                                    break;
                                }
                            }
                        }

//                                    printf("%d\n",op);
//                                    printf("%s\n",str_tmp3);
//                                    printf("%s\n",B_add);
                                    if(str_equal(str_tmp3,B_times,strlen(B_times))){//B-times命令
            //                                printf("111111111111111");
            //                        printf("%d\n",str_value[b_idx]);
            //                        printf("%d\n",str_value[a_idx]);
                                        int z;
                                        for(z=1;z<=str_value[b_idx];z++){
                                            str_value[cur_idx]=z*str_value[a_idx];
                                            if(op==1)
                                                printf("%d\n",str_value[cur_idx]);
                                                }
                                    }
                                    if(str_equal(str_tmp3,B_add,strlen(B_add))){        //B-add命令
            //                                printf("111111111111111");
//                                    printf("%d\n",str_value[b_idx]);

                                    int x=str_value[a_idx];
                                        for( str_value[cur_idx]=1;str_value[cur_idx]<x;str_value[cur_idx]++){
                                            str_value[cur_idx]=str_value[cur_idx]+tmp;
                                            if(op==1) printf("%d\n",str_value[cur_idx]);
                                                }
                                    }

                        }



                        f=0;
//                        printf("3:%s\n",str_tmp3);
//                        printf("2:%s\n",str_tmp2);
                        int  i;
                        for(i=0;i<idx;i++){
//                                printf("i: %d  %s\n",i,str_name[i]);
                            if(str_equal(str_tmp3,str_name[i],strlen(str_tmp3))){
                                b_idx=i;f+=1;
                            }else if(str_equal(str_tmp2,str_name[i],strlen(str_tmp2))){
                                a_idx=i;f+=2;
                            }
                        }
                        fscanf(fp, "%s ", str_tmp3);// operation
//                       printf("%s",str_tmp3);
                        fscanf(fp, "%s ", str_tmp2);//;
                        fscanf(fp, "%s ", str_tmp2);//


                        int op=-1;



                        if(str_equal(str_tmp2,print,strlen(str_tmp2))){
                            op=1;
                            fscanf(fp, "%s ", str_tmp2);//获取$F
                            int z;
                            for(z=0;z<idx;z++){
                                if(strcmp(str_tmp2,str_name[z])==0){
                                    break;
                                }
                            }
                        }

//                        printf("%d\n",op_idx);
//                        printf("%s\n",str_tmp3);
//                        printf("%s\n",B_times);
                        if(str_equal(str_tmp3,B_times,strlen(B_times))){
//                                printf("111111111111111");
//                        printf("%d\n",str_value[b_idx]);
//                        printf("%d\n",str_value[a_idx]);
                        int  x;
                        int z;
                        for(x=1;x<=str_value[wai_idx];x++)
                            for(z=1;z<=str_value[b_idx];z++){
                                str_value[cur_idx]=z*x;
                                if(op==1) printf("%d\n",str_value[cur_idx]);
                                    }
                        }
                        if(str_equal(str_tmp3,B_add,strlen(B_add))){
//                                printf("111111111111111");
//                        printf("%d\n",str_value[b_idx]);
//                        printf("%d\n",str_value[a_idx]);
                            int  x;
                            int z;
                           for(x=1;x<=str_value[wai_idx];x++)
                            for(z=1;z<=str_value[b_idx];z++){
                                str_value[cur_idx]=z+x;
                                if(op==1) printf("%d\n",str_value[cur_idx]);
                                    }
                        }



                    }
                    if(t==-1)
                    idx++;
            }
            fscanf(fp, "%s ", str1);//$F

                    int t=-1;
                    for( i=0;i<idx;i++){
                        if(str_equal(str_name[i],str1,strlen(str1))){
                            t=i;
                        }
                    }
//                    printf("%d\n",t);

                    int cur_idx;
                    if(t==-1)
                    {strcpy(str_name[idx],str1);
                        cur_idx=idx;
                    }

                    if(t!=-1){
                        cur_idx=t;
                    }

                    fscanf(fp, "%s ", str_tmp2);//  kongge
                    fscanf(fp, "%s ", str_tmp2);//  val

                    if(str_tmp2[0]!='$'){
                            str_value[cur_idx]=atoi(str_tmp2);

                    }
                    else{
                        int a_idx ;
                        int b_idx ;
                        fscanf(fp, "%s ", str_tmp3);//  val
                        int f=-1;
                        if(str_tmp3[0]!='$'){
                                int tmp = atoi(str_tmp3);
//                                printf("%d\n",tmp);
                            int i;
                            for(i=0;i<idx;i++){
//                                printf("i: %d  %s\n",i,str_name[i]);
                                if(str_equal(str_tmp2,str_name[i],strlen(str_tmp2))){
                                    a_idx=i;f+=1;break;
                                }
                            }
                            fscanf(fp, "%s ", str_tmp3);// operation
//                           printf("%s",str_tmp3);
                            fscanf(fp, "%s ", str_tmp2);//;
                            fscanf(fp, "%s ", str_tmp2);//
                            int op=-1;



                        if(str_equal(str_tmp2,print,strlen(str_tmp2))){
                            op=1;
                            fscanf(fp, "%s ", str_tmp2);//获取$F
                            int  z;
                            for(z=0;z<idx;z++){
                                if(strcmp(str_tmp2,str_name[z])==0){
                                    break;
                                }
                            }
                        }

//                                    printf("%d\n",op);
//                                    printf("%s\n",str_tmp3);
//                                    printf("%s\n",B_add);
                                    if(str_equal(str_tmp3,B_times,strlen(B_times))){
            //                                printf("111111111111111");
            //                        printf("%d\n",str_value[b_idx]);
            //                        printf("%d\n",str_value[a_idx]);
                                        int z;
                                        for(z=1;z<=str_value[b_idx];z++){
                                            str_value[cur_idx]=z*str_value[a_idx];
                                            if(op==1)
                                                printf("%d\n",str_value[cur_idx]);
                                                }
                                    }
                                    if(str_equal(str_tmp3,B_add,strlen(B_add))){
            //                                printf("111111111111111");
//                                    printf("%d\n",str_value[b_idx]);

                                    int x=str_value[a_idx];
                                        for( str_value[cur_idx]=1;str_value[cur_idx]<x;str_value[cur_idx]++){
                                            str_value[cur_idx]=str_value[cur_idx]+tmp;
                                            if(op==1) printf("%d\n",str_value[cur_idx]);
                                                }
                                    }

                        }



                        f=0;
//                        printf("3:%s\n",str_tmp3);
//                        printf("2:%s\n",str_tmp2);
                        for(i=0;i<idx;i++){
//                                printf("i: %d  %s\n",i,str_name[i]);
                            if(str_equal(str_tmp3,str_name[i],strlen(str_tmp3))){
                                b_idx=i;f+=1;
                            }else if(str_equal(str_tmp2,str_name[i],strlen(str_tmp2))){
                                a_idx=i;f+=2;
                            }
                        }
                        fscanf(fp, "%s ", str_tmp3);// operation
//                       printf("%s",str_tmp3);
                        fscanf(fp, "%s ", str_tmp2);//;
                        fscanf(fp, "%s ", str_tmp2);//


                        int op=-1;



                        if(str_equal(str_tmp2,print,strlen(str_tmp2))){
                            op=1;
                            fscanf(fp, "%s ", str_tmp2);//获取$F
                            int z;
                            for(z=0;z<idx;z++){
                                if(strcmp(str_tmp2,str_name[z])==0){
                                    break;
                                }
                            }
                        }

//                        printf("%d\n",op_idx);
//                        printf("%s\n",str_tmp3);
//                        printf("%s\n",B_times);
                        if(str_equal(str_tmp3,B_times,strlen(B_times))){
//                                printf("111111111111111");
//                        printf("%d\n",str_value[b_idx]);
//                        printf("%d\n",str_value[a_idx]);
                            int z;
                            for(z=1;z<=str_value[b_idx];z++){
                                str_value[cur_idx]=z*str_value[a_idx];
                                if(op==1) printf("%d\n",str_value[cur_idx]);
                                    }
                        }
                        if(str_equal(str_tmp3,B_add,strlen(B_add))){
//                                printf("111111111111111");
//                        printf("%d\n",str_value[b_idx]);
//                        printf("%d\n",str_value[a_idx]);
                            int z;
                            for(z=1;z<=str_value[b_idx];z++){
                                str_value[cur_idx]=z+str_value[a_idx];
                                if(op==1) printf("%d\n",str_value[cur_idx]);
                                    }
                        }



                    }
                    if(t==-1)
                    idx++;







        }
        if(str_equal(str1,ones,sizeof(ones))){
            fscanf(fp, "%s ", str1);
            ceng = atoi(str1);
            fscanf(fp, "%s ", str1);
            lie = atoi(str1);
            fscanf(fp, "%s ", str_2wei_name);
//            printf("%s\n",str_2wei_name);
            int i,j;
            for(i=0;i<ceng;i++){
                for(j=0;j<lie;j++){
                    arr_2[i][j]=1;
                }
            }


        }

        if(str_equal(str1,set,sizeof(set))){
                char str_tmp1[50];
                char str_tmp2[50];
                char str_tmp3[50];
                fscanf(fp, "%s ", str1);
                if(strcmp(str1,str_2wei_name)!=0){


                    int t=-1;
                    int i;
                    for(i=0;i<idx;i++){
                        if(strcmp(str_name[i],str1)==0){
                            t=i;
                        }
                    }

                    int cur_idx;
                    if(t==-1){
                        cur_idx=idx;
                    }
                    else {
                        cur_idx=t;
                    }
                    strcpy(str_name[cur_idx],str1);
                    fscanf(fp, "%s ", str_tmp2);//  kongge
                    fscanf(fp, "%s ", str_tmp2);//  val

                    if(str_tmp2[0]!='$'){
                            str_value[cur_idx]=atoi(str_tmp2);

                    }
                    else{
                        int a_idx ;
                        int b_idx ;
                        fscanf(fp, "%s ", str_tmp3);//  val
                        int f=0;
                        int i;
                        for(i=0;i<idx;i++){
                            if(strcmp(str_tmp3,str_name[i])==0){
                                b_idx=i;f+=1;
                            }else if(strcmp(str_tmp2,str_name[i])==0){
                                a_idx=i;f+=2;
                            }
                        }
                        fscanf(fp, "%s ", str_tmp3);//  val
                        if(strcmp(str_tmp3,B_times)==0){
                        str_value[idx]=str_value[b_idx]*str_value[a_idx];
                        }



                    }

                                if(t==-1)
                                idx++;
//                                printf("%d--\n",idx);
                }else{

                    fscanf(fp, "%s ", str_tmp2);
                    fscanf(fp, "%s ", str_tmp2);
                    fscanf(fp, "%s ", str_tmp3);
                    int val = atoi(str_tmp3);
//                    printf("%d\n",val);
                    fscanf(fp, "%s ", str_tmp1);
                    if(strcmp(str_tmp1,B_add)==0){
                            int i,j;
                        for(i=0;i<ceng;i++){
                            for(j=0;j<lie;j++){
                                arr_2[i][j]+=val;
                            }
                        }
                    }



                }







//            printf("%s\n",str_name[idx]);
//            printf("%d\n",str_value[idx]);


        }

        if(str_equal(str1,print,sizeof(print))){
            fscanf(fp, "%s ", str1);

            if(str1[0]=='$'){

                if(strcmp(str1,str_2wei_name)!=0){
                        int i;
                    for(i=0;i<idx;i++){
                        if(str_equal(str1,str_name[i],strlen(str1))){
                            printf("%d\n",str_value[i]);
                        }
                    }
                }else{int i,j;
                    for( i=0;i<ceng;i++){
                        for( j=0;j<lie;j++){
                            printf("%d ",arr_2[i][j]);
                        }
                        printf("\n");
                    }
                }
            }else{int i;
               for( i=0;i<strlen(str1);i++){
                if(str1[i]!='"'){
                    printf("%c",str1[i]);
                }
               }
               printf("\n");
            }
        }
//        printf("Read String1 |%s|\n", str1 );
   }




   fclose(fp);

   return(0);
}
