module Servedark
  module Server
    class Api
      def initialize(create_endpoint: nil,
                     delete_endpoint: nil,
                     update_endpoint: nil,
                     read_endpoint: nil,
                     error_response:)
        self.create_endpoint = create_endpoint
        self.delete_endpoint = delete_endpoint
        self.update_endpoint = update_endpoint
        self.read_endpoint = read_endpoint
        self.error_response = error_response
      end

      def call(env)
        if env.get?
          call_read(env)
        elsif env.put?
          call_update(env)
        elsif env.post?
          call_create(env)
        elsif env.delete?
          call_update(env)
        end
      end

      private
      attr_accessor :create_endpoint, :delete_endpoint,
                    :update_endpoint, :read_endpoint,
                    :error_response

      def call_read(env)
        return error_response unless read_endpoint
        read_endpoint.call(env)
      end

      def call_update(env)
        return error_response unless update_endpoint
        update_endpoint.call(env)
      end

      def call_create(env)
        return error_response unless create_endpoint
        create_endpoint.call(env)
      end

      def call_delete(env)
        return error_response unless delete_endpoint
        delete_endpoint.call(env)
      end
    end
  end
end

