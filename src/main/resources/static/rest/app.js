var app = angular.module('CocktailApp', ['ngRoute']);

app.config(function($locationProvider) {
    $locationProvider.hashPrefix('');
});

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'menu.html'
        })
        .when('/login', {
            templateUrl: 'login.html',
            controller: 'LoginController'
        })
        .when('/cocktails', {
            templateUrl: 'cocktail-list.html',
            controller: 'CocktailListController'
        })
        .when('/cocktails/:id', {
            templateUrl: 'cocktail-detail.html',
            controller: 'CocktailDetailController'
        })
        .when('/ingredients', {
            templateUrl: 'ingredient-list.html',
            controller: 'IngredientListController'
        })
        .when('/ingredients/:id', {
            templateUrl: 'ingredient-detail.html',
            controller: 'IngredientDetailController'
        })
        .when('/search', {
            reloadOnSearch: false,
            templateUrl: 'search.html',
            controller: 'SearchController'
        })
        .when('/user/fridge', {
            templateUrl: 'fridge.html',
            controller: 'FridgeController'
        })
        .when('/user/possible', {
            templateUrl: 'possible.html',
            controller: 'PossibleController'
        })
        .otherwise({
            redirectTo: '/'
        });
});

app.controller('CocktailListController', function($scope, $http) {
    $http.get('/api/cocktails')
        .then(function(response) {
            $scope.cocktails = response.data;
        });
});

app.controller('CocktailDetailController', function($scope, $routeParams, $http) {
    $http.get('/api/cocktails/' + $routeParams.id)
        .then(function(response) {
            $scope.cocktail = response.data;
        });
});

app.controller('IngredientListController', function($scope, $http) {
    $http.get('/api/ingredients')
        .then(function(response) {
            $scope.ingredients = response.data;
        });
});

app.controller('IngredientDetailController', function($scope, $routeParams, $http) {
    $http.get('/api/ingredients/' + $routeParams.id)
        .then(function(response) {
            $scope.data = response.data;
        });
});

app.controller('SearchController', function($scope, $http, $location) {
    $scope.query = $location.search().query || '';

    if ($scope.query) {
        // Führe die Suche durch
        $http.get('/api/cocktails/search', { params: { query: $scope.query } })
            .then(function(response) {
                $scope.results = response.data;
            });
    }

    $scope.search = function() {
        $location.search('query', $scope.query);
        $http.get('/api/cocktails/search', { params: { query: $scope.query || '' } })
            .then(function(response) { $scope.results = response.data; });
    };
});

app.controller('FridgeController', function($scope, $http) {
    $scope.updateFridge = function() {
        $scope.inFridge = $scope.fridge.filter(function(ingredient) {
            return ingredient.inFridge;
        });
        $scope.notInFridge = $scope.fridge.filter(function(ingredient) {
            return !ingredient.inFridge;
        });
    }

    $scope.updateIngredient = function(id, inFridge) {
        $scope.busy = true;
        $http.patch('/api/fridge/ingredients/' + id, { inFridge: inFridge })
            .then(function() {
                $scope.fridge.forEach(function(ingredient) {
                    if (ingredient.id === id) ingredient.inFridge = inFridge;
                });
                $scope.updateFridge();
            })
            .finally(function() { $scope.busy = false; });
    };

    // Initialisierung beim Laden der Seite
    $http.get('/api/fridge/ingredients')
        .then(function(response) {
            $scope.fridge = response.data;
            $scope.updateFridge();
        });

    // Funktion zum Hinzufügen einer Zutat zum Kühlschrank
    $scope.addToFridge = function(id) {
        // Aktualisiere die inFridge-Eigenschaft der Zutat
        $scope.updateIngredient(id, true);
    };

    // Funktion zum Entfernen einer Zutat aus dem Kühlschrank
    $scope.removeFromFridge = function(id) {
        $scope.updateIngredient(id, false);
    };
});

app.controller('PossibleController', function($scope, $http) {
    $http.get('/api/fridge/possible')
        .then(function(response) {
            $scope.cocktails = response.data;
        });
});

app.factory('Auth', function($http) {
    var auth = {
        token: null,
        session: {authenticated: false, username: null},
        login: function(username, password) {
            return $http.post('/api/auth/login', {username: username, password: password})
                .then(function(response) {
                    auth.token = response.data.token;
                    auth.session.authenticated = true;
                    auth.session.username = username;
                });
        },
        logout: function() {
            auth.token = null;
            auth.session.authenticated = false;
            auth.session.username = null;
        }
    };
    return auth;
});

app.controller('LoginController', function($scope, $location, Auth) {
    $scope.login = function() {
        $scope.busy = true;
        $scope.error = null;
        Auth.login($scope.username, $scope.password)
            .then(function() { $location.path('/user/fridge'); })
            .catch(function(response) {
                $scope.error = response.status === 401
                    ? 'Benutzername oder Passwort ist falsch.'
                    : 'Anmeldung fehlgeschlagen.';
            })
            .finally(function() { $scope.busy = false; $scope.password = ''; });
    };
});

app.config(function($httpProvider) {
    $httpProvider.interceptors.push(function($q, $rootScope, $injector, $location) {
        return {
            request: function(request) {
                var auth = $injector.get('Auth');
                if (request.url.indexOf('/api/') === 0 && auth.token) {
                    request.headers = request.headers || {};
                    request.headers.Authorization = 'Bearer ' + auth.token;
                }
                return request;
            },
            responseError: function(response) {
                if (response.status === 401 && response.config.url !== '/api/auth/login') {
                    $injector.get('Auth').logout();
                    $location.path('/login');
                    $rootScope.apiError = 'Bitte erneut anmelden. Der Token fehlt oder ist abgelaufen.';
                } else if (response.status === 403) {
                    $rootScope.apiError = 'Für diese Aktion fehlt die Berechtigung.';
                } else if (response.config.url !== '/api/auth/login') {
                    $rootScope.apiError = (response.data && response.data.detail) || 'Die Anfrage konnte nicht ausgeführt werden.';
                }
                return $q.reject(response);
            }
        };
    });
});

app.run(function($rootScope, $location, Auth) {
    $rootScope.session = Auth.session;
    $rootScope.sessionReady = true;
    $rootScope.$on('$routeChangeStart', function() { $rootScope.apiError = null; });
    $rootScope.logout = function() {
        Auth.logout();
        $location.path('/');
    };
});
