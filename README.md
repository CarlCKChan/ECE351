# Simple-VHDL-parser-transformer-simulator
Compiler course: Simple VHDL parser, transformer, and visualizer.

1. This parses a subset of VHDL, desugars/elaborates on it, and converts it into an intermediate language "F".
2. "F" takes the form of a series of boolean equations.  These are then transformed using methods such as: expansion into 2-SAT form, dead store elimination, common subexpression elimination, and constant folding.
3. Java source is then generated and executed to run the "F" programs against test data.  This results in a "W" file being created, representing the resulting waveform.
4. The "W" program is then visualized.
