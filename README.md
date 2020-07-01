# Urban Dictionary
An interview coding exercise.

## Requirements:
- Include a text input for users to enter the term they wish to search
- Show the list of resulting definitions, with number of thumbs up and thumbs down votes
	* Use a RecyclerView for this list
- Allow users to sort by either most thumbs up or down
- Show a progress indication while the Urban Dictionary API call is being made
- Write at least one unit test and one instrumented test
	* Be prepared to talk about what other test cases you think would be beneficial to write
- Document ass assumptions

## Optional Requirements

Feel free to add any functionality and refinements that you feel would showcase your abilities
- Save instance state
- Handle configuration changes gracefully
- Cache results for limited offline operation
- UI design, if that is a strength
- Dependency Injection

## Important Setup Information

This app requires an API key from RapidAPI.  I have not included mine for security/privacy reasons.  In order to have this app work, you will need to provide your own API key.  The setup for this is done in the app level `build.gradle` file, where it looks for a property `RapidApiKey` in the user's global `gradle.properties` file.  This file can be found (or created) at `USER_HOME/.gradle/gradle.properties` on Windows machines.  You will need to add the line `RapidApiKey = "my-api-key"` to this file.
