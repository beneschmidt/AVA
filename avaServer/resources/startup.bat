 for /l %%x in (1, 1, 3) do (
    start cmd /k java -jar Node.jar ueb1_input.txt graph.gv
   )