var app = angular.module("myApp");

app.controller('homeController', ['$scope','$state' ,'$rootScope', '$location','MessageService', function($scope, $state, $rootScope, $location, MessageService) {
	$scope.loaded = 0;
	
	$scope.insert = function(message) {
		MessageService.insert(message).success(function(respond) {
			$scope.callback = respond;
			console.log("insert " + respond);
			$state.reload();
		})
	}
	$scope.calculate = function(text) {
		MessageService.calculate(text).success(function(respond){
			$scope.owner = respond;
			console.log(respond);
		})
	}
	$scope.calculate2 = function(text) {
		MessageService.calculate2(text).success(function(respond){
			$scope.owner = respond;
			console.log(respond);
		})
	}
	$scope.getMessages = function()  {
		$scope.loaded += 6;
		MessageService.getAll($scope.loaded).success(function(respond){
			$scope.messages = respond;
			console.log($scope.messages);
		})
	};
}]);