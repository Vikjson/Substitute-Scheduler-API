Author: Viktoria Johansson

To compile and start winstone:
run script: ./compile_servlet_and_start_winstone.sh

To run tests:
run script: ./check_g_or_vg.sh

To search for schedule:
1. Choose format: xml or json
2. Either search without parameters och choose:
 -substitute_id
 -day
 -or both substitute_id and day

Examples:
lwp-request "http://localhost:8080/v1?format=xml"
lwp-request "http://localhost:8080/v1?format=json"
lwp-request "http://localhost:8080/v1?format=json&substitute_id=4"
lwp-request "http://localhost:8080/v1?format=xml&day=2018-01-15"

lwp-request "http://localhost:8080/v1?format=xml&substitute_id=1&day=2018-01-15"
