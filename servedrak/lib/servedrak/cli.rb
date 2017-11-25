require 'thor'

module Servedrak
  class Cli < Thor
    desc 'start', 'Runs the server with Puma'
    def start
      app.start
    end

    desc 'console', 'Opens a console with the app'
    def console
      puts "Use app_tree to access the app"
      require 'pry'; binding.pry
    end

    private

    def app_tree
      @app_tree ||= AppTree.new
    end

    def app
      @app ||= app_tree.app
    end
  end
end
