require 'spec_helper'

RSpec.describe 'Lists Endpoint' do
  describe 'GET /this_endpoint_does_not_exist' do
    let(:path) { "/this_endpoint_does_not_exist" }

    def do_request(params = {})
      get path, params
    end

    it 'should return 404' do
      do_request
      expect(last_response.status).to eq 404
    end

    it 'should return Not Found' do
      do_request
      expect(last_response.body).to eq 'Not Found'
    end
  end
end
