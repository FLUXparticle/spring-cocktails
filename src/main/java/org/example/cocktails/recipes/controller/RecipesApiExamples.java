package org.example.cocktails.recipes.controller;

// Dokumentationsbeispiele aus src/main/resources/cocktails.db.
final class RecipesApiExamples {
    private RecipesApiExamples() {
    }

    static final String COCKTAILS = """
            [
              {
                "id": 1,
                "name": "Orange Velvet"
              },
              {
                "id": 23,
                "name": "Grenadine-Milk"
              },
              {
                "id": 24,
                "name": "Milkshake"
              }
            ]
            """;

    static final String COCKTAIL = """
            {
              "id": 24,
              "name": "Milkshake",
              "instructions": [
                {
                  "amountCL": 0,
                  "ingredient": {
                    "id": 27,
                    "name": "Erdbeeren"
                  }
                },
                {
                  "amountCL": 0,
                  "ingredient": {
                    "id": 30,
                    "name": "Vanilleeis"
                  }
                },
                {
                  "amountCL": 2,
                  "ingredient": {
                    "id": 13,
                    "name": "Barsirup Erdbeere"
                  }
                },
                {
                  "amountCL": 15,
                  "ingredient": {
                    "id": 29,
                    "name": "Milch"
                  }
                }
              ]
            }
            """;

    static final String SEARCH = """
            [
              {
                "id": 22,
                "name": "Bananen-Ananas-Milch"
              },
              {
                "id": 25,
                "name": "Coco-Cherry-Milk"
              },
              {
                "id": 23,
                "name": "Grenadine-Milk"
              },
              {
                "id": 26,
                "name": "Jogging Mix"
              },
              {
                "id": 24,
                "name": "Milkshake"
              },
              {
                "id": 28,
                "name": "Pick Me up"
              },
              {
                "id": 27,
                "name": "Pink Power"
              }
            ]
            """;

    static final String INGREDIENTS = """
            [
              {
                "id": 13,
                "name": "Barsirup Erdbeere"
              },
              {
                "id": 27,
                "name": "Erdbeeren"
              },
              {
                "id": 29,
                "name": "Milch"
              },
              {
                "id": 30,
                "name": "Vanilleeis"
              }
            ]
            """;

    static final String INGREDIENT = """
            {
              "id": 29,
              "name": "Milch",
              "cocktails": [
                {
                  "id": 22,
                  "name": "Bananen-Ananas-Milch"
                },
                {
                  "id": 25,
                  "name": "Coco-Cherry-Milk"
                },
                {
                  "id": 23,
                  "name": "Grenadine-Milk"
                },
                {
                  "id": 26,
                  "name": "Jogging Mix"
                },
                {
                  "id": 24,
                  "name": "Milkshake"
                },
                {
                  "id": 28,
                  "name": "Pick Me up"
                }
              ]
            }
            """;

    static final String FRIDGE = """
            [
              {
                "id": 13,
                "name": "Barsirup Erdbeere",
                "inFridge": true
              },
              {
                "id": 1,
                "name": "Barsirup Mandel",
                "inFridge": false
              },
              {
                "id": 27,
                "name": "Erdbeeren",
                "inFridge": true
              },
              {
                "id": 29,
                "name": "Milch",
                "inFridge": true
              },
              {
                "id": 30,
                "name": "Vanilleeis",
                "inFridge": true
              }
            ]
            """;

    static final String POSSIBLE = """
            [
              {
                "id": 24,
                "name": "Milkshake"
              }
            ]
            """;
}
