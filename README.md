# nrdp

A Naive ride duration prediction program

## Build and Run

**Build**

`$ mvn clean install`

*You need maven installed*

**Run test separately**

`$ mvn test`

**Execute the default command**

`cd ./target`
`java -DinputFile=input.csv -jar nrdp.jar`

*There is a sample csv in the root folder named in.csv. So the command e.g. from the target dir can be executed like `java -DinputFile=../in.csv -jar nrdp.jar`*

*You need java installed*

**Inspect the `out.csv` generated in the same folder**

## Using extra parameters

`java -DinputFile=input.csv -DoutputFile=outfile.csv -jar nrdp.jar` 

`java -DinputFile=input.csv -DoutputFile=outfile.csv -groupByMode=HOURS -jar nrdp.jar`

`java -DinputFile=input.csv -DoutputFile=outfile.csv -DgroupByMode=DAYS_AND_HOURS -jar nrdp.jar` *//DAYS AND HOURS mode is the default*


