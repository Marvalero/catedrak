require_relative 'racks'
require_relative 'tree_endpoints'

module Servedrak
  module Server
    class Tree
      include Treefier::Base
      def rack
        @rack ||= Servedrak::Server::Racks.new(self)
      end

      def endpoints
        @endpoints ||= Servedrak::Server::TreeEndpoints.new(self)
      end
    end
  end
end
