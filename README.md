# Setup

## Required :

- JDK 22+
- Selenium Server Grid 4.21+
- Chrome driver
- Firefox driver

## Run :

1.

    a) Run Selenium Grid `java -jar selenium-server-<version>.jar standalone --detect-drivers true`
    <br/>or <br/>
    b) Run Selenium Grid hub `java -jar selenium-server-<version>.jar hub` and
    node `java -jar selenium-server-<version>.jar node
    --detect-drivers true`

2. Run `testng.xml` test suite