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
	$scope.calculate = function(text, method) {
		MessageService.calculate(text,method).success(function(respond){
			$scope.owner = respond;
			console.log(respond);
		})
	}
	$scope.remove = function(id) {
		MessageService.remove(id).success(function(respond){
			console.log("deleted");
			$state.reload();
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