import {StrictMode, useState} from 'react';
import {createRoot} from 'react-dom/client';
import App from './App.tsx';
import {MantineProvider, ColorSchemeProvider, ColorScheme} from '@mantine/core';

const Root = () => {
    const [colorScheme, setColorScheme] = useState<ColorScheme>('dark');
    const toggleColorScheme = (value?: ColorScheme) =>
        setColorScheme(value || (colorScheme === 'dark' ? 'light' : 'dark'));

    return (
        <StrictMode>
            <ColorSchemeProvider colorScheme={colorScheme} toggleColorScheme={toggleColorScheme}>
                <MantineProvider theme={{colorScheme}} withNormalizeCSS={true} withGlobalStyles={true}>
                    <App />
                </MantineProvider>
            </ColorSchemeProvider>
        </StrictMode>
    );
};

createRoot(document.getElementById('root')!).render(<Root />);