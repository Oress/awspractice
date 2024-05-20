#!/bin/bash

for i in {1..20}; do
	echo "
		{
		    \"firstName\": \"tailor frist $i \",
		    \"lastName\": \"tailor last $i\",
		    \"senderEmail\": \"tailor$i@mail.com\"
		}

	" > payload.json

 	wget --method=PUT --header='Content-Type:application/json' --body-file=payload.json -O- http://localhost:8080/sender

done;
