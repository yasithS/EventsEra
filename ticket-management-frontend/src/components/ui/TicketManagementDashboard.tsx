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

interface ThreadLog {
  id: number;
  threadType: string;
  action: string;
  timestamp: string;
  message: string;
}

const API_BASE_URL = 'http://localhost:8082/api/simulation';

const TicketManagementDashboard: FC = () => {
  const [status, setStatus] = useState<SystemStatus | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [logs, setLogs] = useState<ThreadLog[]>([]);
  const [config, setConfig] = useState<Config>({
    maxTicketPool: 0,
    totalVendors: 0,
    releasePerVendor: 0,
    vendorReleaseRate: 0,
    totalCustomers: 0,
    ticketsPerCustomer: 0,
    customerBuyingRate: 0
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
      
      if (response.ok) {
        const data = await response.json();
        setStatus(data);
        setError('');
      } else {
        const errorText = await response.text();
        console.log('Error response:', errorText);
        setError('System needs to be configured');
      }
    } catch (err) {
      console.error('Status fetch error:', err);
      setError('Failed to fetch system status');
    }
  };

  const fetchLogs = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/logs`);
      if (!response.ok) throw new Error('Failed to fetch logs');
      const data = await response.json();
      setLogs(data);
    } catch (err) {
      console.error('Failed to fetch logs:', err);
    }
  };

  useEffect(() => {
    const statusInterval = setInterval(fetchStatus, 1000);
    const logsInterval = setInterval(fetchLogs, 2000);

    return () => {
      clearInterval(statusInterval);
      clearInterval(logsInterval);
    };
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
      await fetchStatus();
    } catch (err) {
      console.error('Configuration error:', err);
      setError('Failed to update configuration');
    } finally {
      setLoading(false);
    }
  };

  const handleSimulationControl = async (action: 'start' | 'stop'): Promise<void> => {
    setLoading(true);
    setError('');
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
        if (response.status === 400 && errorText.includes("Simulation is already running")) {
          setError("Simulation is already running");
          alert("Simulation is already running. Please stop the current simulation first.");
        } else {
          throw new Error(errorText || `Failed to ${action} simulation`);
        }
      } else {
        console.log(`${action} simulation succeeded`);
      }

      await fetchStatus();
    } catch (err) {
      console.error(`${action} error:`, err);
      setError(`Failed to ${action} simulation: ${err}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-4xl">
      <h1 className="text-2xl font-bold text-center mb-6" id="main">~ EventsEra ~</h1>

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
              <p className="text-sm text-gray-500">Current Pool</p>
              <p className="text-2xl font-bold">{status.currentPoolSize}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Maximum Pool</p>
              <p className="text-2xl font-bold">{status.maximumPoolSize}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Active Vendors</p>
              <p className="text-2xl font-bold">{status.activeVendorThreads}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Active Customers</p>
              <p className="text-2xl font-bold">{status.activeCustomerThreads}</p>
            </div>
            <div className="p-4 bg-gray-50 rounded">
              <p className="text-sm text-gray-500">Total Active Threads</p>
              <p className="text-2xl font-bold">
                {status.activeVendorThreads + status.activeCustomerThreads}
              </p>
            </div>
          </div>
        ) : (
          <div className="text-center text-gray-500">
            {error ? (
              <p className="text-red-500">{error}</p>
            ) : (
              <div className="flex justify-center items-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
                <p className="ml-2">Loading status...</p>
              </div>
            )}
          </div>
        )}
      </div>

      <div className="bg-white shadow rounded-lg p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Configuration Form</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {Object.entries(config).map(([key, value]) => (
            <div key={key}>
              <label className="block text-sm font-medium text-gray-700">
                {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
              </label>
              <input
                id="inputForm"
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
        <div id='submitButton' className="flex justify-center">
          <button 
            onClick={handleSubmitConfig}
            className="mt-6 px-6 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? 'Updating...' : 'Update Configuration'}
          </button>
        </div>
      </div>

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

      {error && !status && (
        <div className="bg-red-50 text-red-500 p-4 rounded mb-6">
          {error}
        </div>
      )}

      <div className="bg-white shadow rounded-lg p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Thread Logs</h2>
        <div className="h-64 overflow-y-auto">
          {logs.length > 0 ? (
            logs.map(log => (
              <div 
                key={log.id} 
                className={`p-2 mb-2 rounded ${
                  log.threadType.includes('VENDOR') ? 'bg-blue-50' : 
                  log.threadType.includes('CUSTOMER') ? 'bg-green-50' : 
                  'bg-yellow-50'
                }`}
              >
                <div className="flex justify-between text-sm">
                  <span className="font-medium">
                    {log.threadType} - {log.action}
                  </span>
                  <span className="text-gray-500">
                    {new Date(log.timestamp).toLocaleTimeString()}
                  </span>
                </div>
                <p className="text-sm text-gray-600">{log.message}</p>
              </div>
            ))
          ) : (
            <p className="text-center text-gray-500">No logs available</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default TicketManagementDashboard;