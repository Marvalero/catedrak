require_relative 'endpoint'
require_relative 'api'
require 'json'

module Servedrak
  module Server
    class TreeEndpoints
      include Treefier::Base
      RESPONSE_HEADERS = { 'Content-Type'.freeze => 'text/plain'.freeze }.freeze
      JSON_RESPONSE_HEADERS = { 'Content-Type'.freeze => 'application/json'.freeze }.freeze
      PING_RESPONSE = [200.freeze, RESPONSE_HEADERS, ['pong'].freeze].freeze
      NOT_FOUND_RESPONSE = [404.freeze, RESPONSE_HEADERS, ['Not Found'].freeze].freeze
      NOT_FOUND_JSON_RESPONSE = [404.freeze, JSON_RESPONSE_HEADERS, [{'error' => 'Not Found'}.to_json].freeze].freeze

      def show_list
        @show_list ||= build_endpoint(self.lists.actions.show,
                                      -> (items) { [200, JSON_RESPONSE_HEADERS, [ items.map { item.to_h }.to_json ]] })
      end

      def lists_api
        @lists_api ||= build_api(read: show_list)
      end

      def ping
        @ping ||= build_endpoint(-> (_params) {},
                                 -> (_params) { PING_RESPONSE })
      end

      def not_found
        @not_found ||= build_endpoint(-> (_params) {},
                                   -> (_params) { NOT_FOUND_RESPONSE })
      end

      private

      def build_endpoint(action, response_handler)
        endpoint_class.new(action: action, response_handler: response_handler)
      end

      def build_api(create: nil, update: nil, read: nil, delete: nil)
        api_class.new(create_endpoint: create,
                      delete_endpoint: delete,
                      update_endpoint: update,
                      read_endpoint: read,
                      error_response: NOT_FOUND_JSON_RESPONSE)
      end

      def api_class
        @api_class ||= Servedrak::Server::Api
      end

      def endpoint_class
        @endpoint_class ||= Servedrak::Server::Endpoint
      end
    end
  end
end
