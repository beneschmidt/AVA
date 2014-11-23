 for /l %%x in (1, 1, 20) do (
    start cmd /k java -jar Business.jar ueb2_input.txt graph.gv %%x
 )