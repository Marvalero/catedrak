module Servedrak
  class App
    class << self
      attr_accessor :rack
    end

    def initialize(configurator)
      self.port = configurator.config['server']['port']
      self.rack = configurator.server.rack.main
    end

    def start
      start_server
    end

    private
    attr_accessor :port, :rack

    def start_server
      self.class.rack = rack
      require 'puma/cli'
      Puma::CLI.new(['-p', port.to_s]).run
    end
  end
end
