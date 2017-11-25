module Peppaframe
  module BaseActions
    class Create
      def initialize(configurator)
        self.collection = configurator.collection
        self.factory = configurator.factory
      end

      def call(data)
        factory.build(data).tap do |e|
          collection << e
        end
      end

      private
      attr_accessor :collection, :factory
    end
  end
end
