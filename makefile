JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	demo3d.java\
	screenRender.java\
	mapLoader.java\
	line3d.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class

