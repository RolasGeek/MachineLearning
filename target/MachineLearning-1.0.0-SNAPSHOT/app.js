

var app = angular.module("myApp", ['ui.router','ui.bootstrap', 'ngResource','infinite-scroll']);


app.config(['$stateProvider','$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider) {
	$urlRouterProvider.otherwise('/home');
    $stateProvider
    .state('home', {
    	url: '/home',
    	templateUrl: 'pages/home/home.html',
    	controller: 'homeController',
    	cache: false
    });
}]);

app.run(function($rootScope, $state, $location, $http) {
	
});






