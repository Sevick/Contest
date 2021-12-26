How to use:
   -help - show this help
   -inputfile=[FILE] - use file as input (stdin if not specified)
   -outputfile=[FILE] - use file as input (stdout if not specified)

   All available configuration properties are listen in application.yml file with their default value.

   Application read list of tests confirations from stdin, writing test resutls to stdout and all logging messages to stderr.

   Supporter tests configuration example:
	 DNS
		{"type":"dns","address":"http://yahoo.com"}
	 HTTP
		{"type":"http","address":"http://yahoo.com","httpMethod":"HEAD","expectedResultCode":301,"measureLatency":false,"measureBandwidth":true}
	 HTTPS
		{"type":"https","address":"https://yahoo.com","httpMethod":"HEAD","expectedResultCode":301,"measureLatency":false,"measureBandwidth":true}

Writers:
   By default logfile will be created according to requirements.
   If my proposal to use standart unix pipelines instead - profile "skiplogger" and be used to deactive TestResultWriterFile service (e.g. add "-Dspring.profiles.active=skiplogger" parameter).
   However if used this service can be confiured with
		testresultwriterfile.logfile=contest.log
		testresultwriterfile.ignoreWriteErrors=false
	ignoreWriteErrors allows to continue processing in case of write failure.

   Writer to stdout (TestResultWriterOutput) has only one configurable option
		testresultwriteroutput.ignoreWriteErrors=false