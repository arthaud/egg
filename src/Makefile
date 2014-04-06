EGGJAR=./eggc-5.3.1.jar
OUTJAR=./eggc.jar
JDIR=/usr/bin
JAVAC=$(JDIR)/javac -O -Xlint:unchecked -encoding UTF-8
JAVA=$(JDIR)/java
JAR=$(JDIR)/jar
JAVADOC=$(JDIR)/javadoc

# langages
XLANG=EGG
OLANG=OPTION
LLANG=LACTION
ELANG=EXPREG

#################

all : ljava package class legg comp jar

ljava: 
	$(JAVAC) -classpath . mg/egg/eggc/runtime/libjava/*.java
	$(JAVAC) -classpath . mg/egg/eggc/runtime/libjava/lex/*.java
	$(JAVAC) -classpath . mg/egg/eggc/runtime/libjava/problem/*.java
	$(JAVAC) -classpath . mg/egg/eggc/runtime/libjava/messages/*.java

legg: 
	$(JAVAC) -classpath . \
	mg/egg/eggc/compiler/libegg/base/*.java \
	mg/egg/eggc/compiler/libegg/java/*.java \
	mg/egg/eggc/compiler/libegg/jlex/*.java \
	mg/egg/eggc/compiler/libegg/type/*.java \
	mg/egg/eggc/compiler/libegg/egg/*.java \
	mg/egg/eggc/compiler/libegg/latex/*.java

package:
	cd mg/egg/eggc/compiler/egg && \
	$(JAVA) -jar ../../../../../$(EGGJAR) $(XLANG).egg && \
	$(JAVA) -jar ../../../../../$(EGGJAR) $(OLANG).egg && \
	$(JAVA) -jar ../../../../../$(EGGJAR) $(LLANG).egg && \
	$(JAVA) -jar ../../../../../$(EGGJAR) $(ELANG).egg 

class: mg/egg/eggc/compiler/egg/java
	$(JAVAC) -classpath $(EGGJAR) mg/egg/eggc/compiler/egg/java/*.java

comp:
	$(JAVAC) -classpath . mg/egg/eggc/compiler/*.java 

jar: mg/egg/eggc/compiler/egg/java
	$(JAR) cmf MANIFEST.MF $(OUTJAR) \
	mg/egg/eggc/compiler/*.class \
	mg/egg/eggc/compiler/egg.dtd \
	mg/egg/eggc/compiler/egg/*.properties \
	mg/egg/eggc/compiler/egg/java/*.class \
	mg/egg/eggc/compiler/libegg/base/*.class \
	mg/egg/eggc/compiler/libegg/java/*.class  \
	mg/egg/eggc/compiler/libegg/jlex/*.class \
	mg/egg/eggc/compiler/libegg/type/*.class \
	mg/egg/eggc/compiler/libegg/egg/*.class \
	mg/egg/eggc/compiler/libegg/latex/*.class \
	mg/egg/eggc/runtime/libjava/*.class \
	mg/egg/eggc/runtime/libjava/lex/*.class \
	mg/egg/eggc/runtime/libjava/problem/*.class \
	mg/egg/eggc/runtime/libjava/messages/*.class \
	mg/egg/eggc/runtime/libjava/messages/*.properties

clean:
	rm -rf \
	$(OUTJAR) \
	mg/egg/eggc/compiler/egg/java \
	mg/egg/eggc/compiler/*.class \
	mg/egg/eggc/compiler/libegg/base/*.class \
	mg/egg/eggc/compiler/libegg/java/*.class  \
	mg/egg/eggc/compiler/libegg/jlex/*.class \
	mg/egg/eggc/compiler/libegg/type/*.class \
	mg/egg/eggc/compiler/libegg/egg/*.class \
	mg/egg/eggc/compiler/libegg/latex/*.class \
	mg/egg/eggc/runtime/libjava/*.class \
	mg/egg/eggc/runtime/libjava/lex/*.class \
	mg/egg/eggc/runtime/libjava/problem/*.class \
	mg/egg/eggc/runtime/libjava/messages/*.class
