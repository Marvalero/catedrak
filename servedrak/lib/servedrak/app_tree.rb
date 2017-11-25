require_relative 'app'
require 'yaml'
require_relative 'server/tree'

module Servedrak
  class AppTree
    def server
      @server ||= Servedrak::Server::Tree.new(self)
    end

    def app
      @app ||= Servedrak::App.new(self)
    end

    def config
      @config ||= YAML.load_file('config/application.yml')
    end
  end
end

