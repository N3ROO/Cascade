# Cascade  ![Version](https://img.shields.io/badge/Code_version-2.0-green.svg) ![Android](https://img.shields.io/badge/Android_version-5.0+-green.svg) ![Android API](https://img.shields.io/badge/Android_API-21+-green.svg) ![Licence](https://img.shields.io/badge/License-GNU_GPL_3.0-blue.svg)

The goal of the game is to destroy a brick wall in a as less clicks as possible. But ... care to not let unbreakable bricks...

## Explanations

- You can't destroy a single brick : the brick must have at least a same color neighbor,
- The top 10 scores are visible on the scoreboard page
- For the score calculation, see above.

## Score calculation

| Number of cell removed (nb) | Score   |
|-----------------------------|---------|
| nb = 2                      | 5       |
| 2 <= nb < 4                 | nb * 10 |
| 4 <= nb < 6                 | nb * 15 |
| 6 <= nb < 8                 | nb * 20 |
| 8 < nb                      | nb * 30 |

When the game finishes, so when there is no more cell to remove, we remove (number of cells remaining * 50) to the score.
If there is no cell remaining on the grid, the user gets a +500 score bonus.

## Information about updates

Check the [changelog](CHANGELOG.md) to get an idea of the advancement and the objectives of each update.

## External resources used

- An extremely lightweight particle free to use library, [LeonidsLib](http://plattysoft.github.io/Leonids/)
- Some logos and symbols, [see details](ui/credits.txt)
- High quality royalty free sound effects from [Sonniss.com](https://sonniss.com/gdc-bundle-license/)

## App design

The design has been entirely made by Lilian Gallon, all the logos and symbols used have been modified to be uniformed.
The UI production used the same licence as the one of the project. [See details](LICENSE.md)