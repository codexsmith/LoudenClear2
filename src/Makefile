spec=sample_spec.txt
file=sample_input.txt

all: compile
	
compile:
	javac *.java
run: compile
	java Driver $(spec) $(file)
clean:
	rm -f *.class
