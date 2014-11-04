 for /l %%x in (1, 1, 4) do (
    start cmd /k java -jar Node.jar ueb1_input.txt graph.gv
   )