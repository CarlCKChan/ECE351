# see these links:
# http://geosoft.no/development/javamake.html
# http://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html

.SUFFIXES: .java .class
.PHONY : classes clean 

BIN_DIR = bin
SOURCES = $(shell find src/ -type f -name '*.java' | sort) 
CLASSES = $(subst src/,bin/,$(SOURCES:.java=.class))
JARS = $(shell find lib/ -type f -name '*.jar' -printf '%p:')

classes: $(BIN_DIR) $(CLASSES)
	@echo compiled Java classes successfully.

bin/%.class : src/%.java
	nice javac -g -d bin/ -sourcepath src/ -cp bin:$(JARS) $<

$(BIN_DIR):
	mkdir -p bin

clean:
	rm -rf bin

