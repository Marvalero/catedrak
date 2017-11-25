module Peppaframe
  module Server
    class Endpoint
      def initialize(action:, response_handler:)
        self.action = action
        self.response_handler = response_handler
      end

      def call(env)
        env = Rack::Request.new(env) unless env.respond_to?(:params)
        response_handler.call(action.call(env.params))
      end

      private
      attr_accessor :action, :response_handler
    end
  end
end
