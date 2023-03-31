# Tech22 test

## Test description

You need to create a web applica1on that allows users to convert a monetary amount from one currency to another using real-1me exchange rates.
The applica1on should perform the following tasks:
1. The applica1on displays a form with the following input fields:
   • A currency to convert from field where users can select the currency they want
   to convert from.
   • A currency to convert to field where users can select the currency they want to
   convert to.
   • An amount field where users can enter the amount they want to convert.
2. When the user submits the form, the applica1on makes an HTTP GET request to a public API to retrieve the current exchange rate for the two currencies.
3. The applica1on then calculates the converted amount and displays it on the page.
4. The applica1on should handle errors and excep1ons gracefully.
5. The applica1on should include tests to ensure correctness.
6. The code should be clean, maintainable, and easy to read.
   ### Requirements
1. The applica1on should be wriOen in Kotlin using the Ktor framework.
2. The applica1on should use a public API to retrieve real-1me exchange rates. You can
   use any public API of your choice, such as Open Exchange Rates, Fixer.io, or any other
   free or paid API.
3. The applica1on should validate user input and handle errors gracefully.
4. The applica1on should use the appropriate HTTP response codes to indicate the
   success or failure of requests.
5. The applica1on should include tests to ensure correctness, including unit tests and
   integra1on tests.
6. The code should be clean, maintainable, and easy to read.
   ### Submission
   You should submit the code for the applica1on, along with any necessary instruc1ons for building and running the applica1on, and any necessary documenta1on. You should also include test cases for their implementa1on.
   Please submit your submission to andy@tech22solu1ons.com

## How to start

```bash
./gradlew run
```

application will start on http://localhost:8080