import React, { FC, useState, useEffect } from 'react';

interface SystemStatus {
  maximumPoolSize: number;
  currentPoolSize: number;
  availableSpace: number;
  totalTicketsReleased: number;
  totalTicketsBought: number;
  activeVendorThreads: number;
  activeCustomerThreads: number;
}

interface Config {
  maxTicketPool: number;
  totalVendors: number;
  releasePerVendor: number;
  vendorReleaseRate: number;
  totalCustomers: number;
  ticketsPerCustomer: number;
  customerBuyingRate: number;
}

const API_BASE_URL = '/api/simulation';

const TicketManagementDashboard: FC = () => {
  const [status, setStatus] = useState<SystemStatus | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [config, setConfig] = useState<Config>({
    maxTicketPool: 1000,
    totalVendors: 5,
    releasePerVendor: 10,
    vendorReleaseRate: 1,
    totalCustomers: 20,
    ticketsPerCustomer: 2,
    customerBuyingRate: 1
  });

  const fetchStatus = async (): Promise<void> => {
    try {
      const response = await fetch(`${API_BASE_URL}/status`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to fetch status');
      }
      const data = await response.json();
      setStatus(data);
      setError('');
    } catch (err) {
      console.error('Status fetch error:', err);
      setError('Failed to fetch system status');
    }
  };

  useEffect(() => {
    const interval = setInterval(fetchStatus, 1000);
    return () => clearInterval(interval);
  }, []);

  const handleConfigChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const { name, value } = e.target;
    setConfig(prev => ({
      ...prev,
      [name]: parseInt(value) || 0
    }));
  };

  const handleSubmitConfig = async (): Promise<void> => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/configure`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify(config)
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to configure system');
      }
      setError('');
    } catch (err) {
      console.error('Configuration error:', err);
      setError('Failed to update configuration');
    } finally {
      setLoading(false);
    }
  };

  const handleSimulationControl = async (action: 'start' | 'stop'): Promise<void> => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/${action}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Failed to ${action} simulation`);
      }
      setError('');
      await fetchStatus(); // Refresh status after control action
    } catch (err) {
      console.error(`${action} error:`, err);
      setError(`Failed to ${action} simulation`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-4xl">
      <h1 className="text-2xl font-bold text-center mb-6">Ticket Management System</h1>
      
      <div className="bg-white shadow rounded-lg p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">System Status</h2>
        {status ? (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Available Tickets</p>
              <p className="text-2xl font-bold">{status.availableSpace}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Total Released</p>
              <p className="text-2xl font-bold">{status.totalTicketsReleased}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Total Bought</p>
              <p className="text-2xl font-bold">{status.totalTicketsBought}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Active Threads</p>
              <p className="text-2xl font-bold">
                {status.activeVendorThreads + status.activeCustomerThreads}
              </p>
            </div>
          </div>
        ) : (
          <p className="text-center text-gray-500">Loading status...</p>
        )}
      </div>

      {error && (
        <div className="bg-red-50 text-red-500 p-4 rounded mb-6">
          {error}
        </div>
      )}

      <div className="bg-white shadow rounded-lg p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Controls</h2>
        <div className="flex gap-4 justify-center">
          <button
            onClick={() => handleSimulationControl('start')}
            className="px-6 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Start Simulation'}
          </button>
          <button
            onClick={() => handleSimulationControl('stop')}
            className="px-6 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? 'Processing...' : 'Stop Simulation'}
          </button>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-xl font-semibold mb-4">Configuration</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {Object.entries(config).map(([key, value]) => (
            <div key={key}>
              <label className="block text-sm font-medium text-gray-700">
                {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
              </label>
              <input
                type="number"
                name={key}
                value={value}
                onChange={handleConfigChange}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                min="0"
                disabled={loading}
              />
            </div>
          ))}
        </div>
        <button
          onClick={handleSubmitConfig}
          className="mt-6 px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:opacity-50"
          disabled={loading}
        >
          {loading ? 'Updating...' : 'Update Configuration'}
        </button>
      </div>
    </div>
  );
};

export default TicketManagementDashboard;