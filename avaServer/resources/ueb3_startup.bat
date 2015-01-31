for /l %%x in (1, 1, 10) do (
start cmd /k java -jar Resource.jar ueb3_input.txt %%x
)