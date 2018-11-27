# Tool-chain for Software Visualization
아래과 같은 도구로 Tool-chain을 구성한다.
![toolchain](./toolchain.png)
* Source Navigator

소스 네비게이터는 파서로써 소스 코드를 스캔하고 기존 C, C++, Java, Tcl, FORTRAN, COBOL 등과 같은 프로그램에서 정보를 추출하여 dbdump를 생성한다.

* SQLite

소스 네이게이터를 통해 분석된 데이터를 데이터베이스화 하는데 SQLite를 사용한다.

* Graphviz (Dot Script)

소프트웨어 품질 지표나 출력하고 싶은 정보를 Dot 스크립트를 작성하여 그래프화 시켜주는 도구이다.

## Getting Started

자바, C/C++과 같은 프로그램을 소스 네비게이터를 통해 구문 분석을 하고, 그 결과로 나온 SNDB 파일을 SQLite 데이터베이스에 저장하고 필요한 정보를 추출하여 Dot 스크립트를 통해 그래프로 소프트웨어 가시화를 한다. 

### Installing

SWV에서 전체 필요한 프로그램을 설치했는데, 혹시라도 누락된 경우가 있다면 아래와 같이 설치를 하면 된다. 리눅스에 SQLite는 기본으로 설치되어 있으므로 따로 설치방법을 설명하지 않고, Source Navigator, Graphviz만 설명한다. SWV에서 디렉토리 구성을 모두 했으므로 아래와 같이 따라 한다.
만약 /usr/local/SWV/dev 디렉토리가 없다면, 디렉토리를 생성하고 작업해야 한다.

* Source Nagigator

```bash
# cd /usr/local/SWV/dev
# mkdir SNavi
# cd SNavi
# wget https://sourceforge.net/projects/sourcenav/files/NG4.5/sourcenavigator-NG4.5.tar.bz2
# tar jxf sourcenavigator-NG4.5.tar.bz2
# cd sourcenavigator-NG4.5
# ./configure --prefix=/usr/local/SWV/dev/SNavi
# make
# make install

```

* Graphviz

```bash
# cd /usr/local/SWV/dev
# mkdir graphviz
# cd graphviz
# wget -c http://graphviz.gitlab.io/pub/graphviz/stable/SOURCES/graphviz.tar.gz
# tar -xzvf graphviz.tar.gz
# cd graphviz-2.40.1
# ./configure
# make
# make install

```

* toolchain 소스코드를 github에서 저장소를 복제한다.
복제 위치: /usr/local/SWV/toolchain 이므로 아래와 같이 작성한다. 복제가 끝난 후, 확인하면 본 toolchain 소스코드가 저장된 것을 확인할 수 있다.

```bash
# git clone https://github.com/moasoftware/toolchain.git /usr/local/SWV/toolchain
```

## Running the tests

1. AnnotationTerminator

1.1 AnnotationTerminator Compile

Source Nagigator 가 Annotaion 기능에 대해 구문분석을 못하므로  소스코드에서 Annotation을 삭제하는 작업이 필요하다.
**"/usr/local/SWV/toolchain/AnnotationTerminator/lib/javaparser-core-3.1.1.jar"** 라이브러리를 포함하여 
**/usr/local/SWV/toolchain/AnnotationTerminator/bin/** 디렉토리에 컴파일한 **.class** 파일을 위치시켜 컴파일하도록 한다.

```bash
# javac -cp "/usr/local/SWV/toolchain/AnnotationTerminator/lib/javaparser-core-3.1.1.jar" -sourcepath src -d /usr/local/SWV/toolchain/AnnotationTerminator/bin/ /usr/local/SWV/toolchain/AnnotationTerminator/src/open/swv/annotation_terminator/*.java
```

1.2 AnnotationTerminator.jar 생성
```bash
추가
```

1.3 AnnotationTerminator 실행

**AnnotationTerminator.jar**는 **/usr/local/SWV/toolchain**에 있다.
분석할 소스코드는 자바로 만든 Chess 프로그램이고, 소스코드는 **/usr/local/SWV/dev/** 하위에 존재한다.
분석할 소스코드가 있는지 꼭 확인하고 아래처럼 실행한다. 절대경로로 실행하는 것은 어느 위치에 있든지 헷갈리지 않고 프로그램을 실행하려는데 목적이 있다.

```bash
java -jar /usr/local/SWV/toolchain/AnnotationTerminator.jar -input /usr/local/SWV/dev/all_java_uci_ce/uci/MagnumChess_v4.00/src/magnumchess -output /usr/local/SWV/dev/src
```

2. filelist.dat 생성

filelist.dat 파일이 있다면 삭제를 하고, **/usr/local/SWV/dev/src** 하위의 파일목록으로 filelist.dat를 생성한다.

```bash
# rm -rf /usr/local/SWV/dev/filelist.dat
# echo /usr/local/SWV/dev/src/* > /usr/local/SWV/dev/filelist.dat
# cat /usr/local/SWV/dev/filelist.dat
/usr/local/SWV/dev/src/Bitbase.java 
/usr/local/SWV/dev/src/Board.java 
/usr/local/SWV/dev/src/Engine.java 
/usr/local/SWV/dev/src/Evaluation2.java 
/usr/local/SWV/dev/src/Global.java 
/usr/local/SWV/dev/src/HistoryWriter.java 
/usr/local/SWV/dev/src/Main.java
/usr/local/SWV/dev/src/MoveFunctions.java 
/usr/local/SWV/dev/src/SEE.java 
/usr/local/SWV/dev/src/TransTable.java
```
 
filelist.dat 파일이 올바르게 만들어졌는지 확인한다.
소스코드의 경로가 줄 단위로 있어야 한다. 만약 줄 단위가 아니라면 직접 수정을 하도록 하자.

3. Source Navigator 실행

```bash
# /usr/local/SWV/dev/SNavi/bin/snavigator --batchmode --import /usr/local/SWV/dev/filelist.dat

Scanning Project...
Scanning: /usr/local/SWV/dev/src/Bitbase.java
Scanning: /usr/local/SWV/dev/src/Board.java
Scanning: /usr/local/SWV/dev/src/Engine.java
Scanning: /usr/local/SWV/dev/src/Evaluation2.java
Scanning: /usr/local/SWV/dev/src/Global.java
Scanning: /usr/local/SWV/dev/src/HistoryWriter.java
Scanning: /usr/local/SWV/dev/src/Main.java
Scanning: /usr/local/SWV/dev/src/MoveFunctions.java
Scanning: /usr/local/SWV/dev/src/SEE.java
Scanning: /usr/local/SWV/dev/src/TransTable.java
Waiting for cross-referencing to complete
Project has been built
```

4. ExtractInfo

4.1 ExtractInfo Compile

Source Nagigator 가 소스코드를 분석하여 dbdump를 생성하고, 그 내용을 SQLite에 저장한다.

```bash
# javac -cp "/usr/local/SWV/toolchain/ExtractInfo/lib/sqlite-jdbc-3.8.11.2.jar" -sourcepath src -d /usr/local/SWV/toolchain/ExtractInfo/bin/ /usr/local/SWV/toolchain/ExtractInfo/src/open/swv/main/ExtractInfo.java
```

4.2 ExtractInfo.jar 생성
```bash
추가
```

4.3 ExtractInfo 실행

**ExtractInfo.jar**는 **/usr/local/SWV/toolchain**에 있다. 여기에서 꼭 해야할 일이 있는데, Source Navigator 의 ```dbdump``` 명령어를 사용하려면, 링크를 먼저 해줘야 한다.

```bash
# ln /usr/local/SWV/dev/SNavi/sourcenavigator-NG4.5/snavigator/db/dbdump /usr/local/SWV/dev/SNavi/bin/dbdump
```

```bash
# java -jar /usr/local/SWV/toolchain/ExtractInfo.jar -sndb "/usr/local/SWV/dev/SNavi/bin/SNDB4" -sndump "/usr/local/SWV/dev/SNavi/bin/dbdump"
db name is default : recoveryDB.db
init db: true
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/tmp_n7tdf0
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/tmp_CXE6CQ
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.icl
Insert DB : icl file

Complete : icl file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.f
Insert DB : f file

Complete : f file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.iu
Insert DB : iu file

Complete : iu file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.fil
Insert DB : fil file

Complete : fil file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.iv
Insert DB : iv file

Complete : iv file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.mi
Insert DB : mi file

Complete : mi file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.md
Insert DB : md file

Complete : md file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.lv
Insert DB : lv file

Complete : lv file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.cl
Insert DB : cl file

Complete : cl file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.to
Insert DB : to file
..
Complete : to file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.by
Insert DB : by file
..
Complete : by file
/usr/local/SWV/dev/SNavi/bin/dbdump /usr/local/SWV/dev/SNavi/bin/SNDB4/bin.1
init db: true

```

실행 후, 만들어지는 ```recoveryDB.db``` 파일은 위에 jar를 실행한 위치에 생성이 되므로 꼭 주의해서 ```/usr/local/SWV/dev``` 디렉토리 위치에서 실행해야 한다. 다른 곳에 생성되면 본인이 찾으러 다녀야하는 번거로움이 발생!!!


5. Diagram 

5.1 Diagram Compile

```bash
# javac -cp "/usr/local/SWV/toolchain/Diagram/lib/sqlite-jdbc-3.8.11.2.jar" -sourcepath src -d /usr/local/SWV/toolchain/Diagram/bin/ /usr/local/SWV/toolchain/Diagram/src/open/swv/diagram/*.java
```

5.2 Digram.jar 생성

```bash
추가
```

5.3 Diagram 실행

4번 ExtractInfo를 실행하고 만들어지는 ```recoveryDB.db``` 파일이 ```/usr/local/SWV/dev```에 생성되는데, 이 파일 크기가 0이라면 제대로 생성된 것이 아니므로 꼭 확인을 해본 후에 아래 **Diagram.jar** 실행을 하길 권한다. 그리고 ```dot```명령어가 작성자의 리눅스에는 ```/usr/local/bin/dot```위치에 있어서 아래와 같이 작성하여 실행하였다.

```bash
# java -jar /usr/local/SWV/toolchain/Diagram.jar /usr/local/SWV/dev /usr/local/bin/dot
....
....
...
	"Evaluation2"->"Bitbase" [style=dotted, arrowhead=vee];
	"Evaluation2"->"TransTable" [style=dotted, arrowhead=vee];
	"Main"->"Board" [style=dotted, arrowhead=vee];
	"Main"->"String" [style=dotted, arrowhead=vee];
	"SEE"->"Board" [style=dotted, arrowhead=vee];
	"SEE"->"Global" [style=dotted, arrowhead=vee];
	"String"->"Global" [style=dotted, arrowhead=vee];
}
dotting
/usr/local/SWV/dev/ClassDiagram.png
done
digraph xx {
	node[shape=record, style=filled, fillcolor=lightyellow];
"Bitbase""Board""Evaluation2""Global""Main""SEE""String""TransTable"		"#"->"Global" [style=solid, arrowhead=vee, label="218"];
		"#"->"Board" [style=solid, arrowhead=vee, label="5"];
		"#"->"Evaluation2" [style=solid, arrowhead=vee, label="159"];
		"#"->"String" [style=solid, arrowhead=vee, label="0"];
		"#"->"SEE" [style=solid, arrowhead=vee, label="5"];
		"Board"->"Global" [style=solid, arrowhead=vee, label="24"];
		"Board"->"Bitbase" [style=solid, arrowhead=vee, label="1"];
		"Evaluation2"->"Board" [style=solid, arrowhead=vee, label="57"];
		"Evaluation2"->"Global" [style=solid, arrowhead=vee, label="182"];
		"Evaluation2"->"Bitbase" [style=solid, arrowhead=vee, label="0"];
		"Evaluation2"->"TransTable" [style=solid, arrowhead=vee, label="5"];
		"Main"->"Board" [style=solid, arrowhead=vee, label="0"];
		"Main"->"String" [style=solid, arrowhead=vee, label="0"];
		"SEE"->"Board" [style=solid, arrowhead=vee, label="63"];
		"SEE"->"Global" [style=solid, arrowhead=vee, label="56"];
		"String"->"Global" [style=solid, arrowhead=vee, label="4"];
}
dotting
/usr/local/SWV/dev/CouplingDiagram.png
done
```

```/usr/local/SWV/dev/ClassDiagram.png``` , ```/usr/local/SWV/dev/CouplingDiagram.png``` 파일을 실행하면 툴체인을 통해 생성된 소스코드 가시화 내용을 확인할 수 있다.

![Coupling](./CouplingDiagram.png)

