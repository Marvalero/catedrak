require 'rack'

module Servedrak
  module Server
    class Racks
      def initialize(configurator)
        self.endpoints = configurator.endpoints
      end

      def main
        _endpoints = endpoints

        Rack::Builder.new do
          use Rack::CommonLogger
          use Rack::ShowExceptions

          map '/lists' do
            run _endpoints.lists_api
          end

          map '/ping' do
            run _endpoints.ping
          end

          run _endpoints.not_found
        end
      end

      private
      attr_accessor :endpoints
    end
  end
end

