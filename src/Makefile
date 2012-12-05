spec=sample_spec.txt
file=sample_input.txt

all: compile
	
compile:
	javac *.java -Xlint
run: compile
	java Driver $(spec) $(file)
clean:
	rm -f *.class
