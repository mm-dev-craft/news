import React from 'react';
import { Card, Text, Anchor, Group, Badge, Divider } from '@mantine/core';
import { IconExternalLink } from '@tabler/icons-react';
import HeadlineData from "./headlineData.ts";
import { useStore } from "../store";

const Headline: React.FC<HeadlineData> = ({ title, link, source, crawlingDate, keywords }) => {
    const formattedDate = new Date(crawlingDate).toLocaleDateString('de-DE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });

    const { selectedKeywords, toggleKeyword } = useStore();

    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            <Group position="apart" style={{ marginBottom: 5 }}>
                <Text weight={500}>{title}</Text>
            </Group>
            <Group position="apart" style={{ marginTop: 10 }}>
                <Group spacing="xs" align="center">
                    <Badge color="blue" variant="filled" size="lg">
                        {source}
                    </Badge>
                    <Divider orientation="vertical" style={{ margin: '0 8px' }} />
                    {keywords.map((keyword, index) => (
                        <Badge
                            key={index}
                            color={selectedKeywords.includes(keyword) ? "green" : "blue"}
                            variant="light"
                            style={{ cursor: 'pointer' }}
                            onClick={() => toggleKeyword(keyword)}
                        >
                            #{keyword}
                        </Badge>
                    ))}
                    <Divider orientation="vertical" style={{ margin: '0 8px' }} />
                    <Anchor
                        href={link}
                        target="_blank"
                        rel="noopener noreferrer"
                        style={{ display: 'flex', alignItems: 'center' }}
                    >
                        <IconExternalLink size={16} />
                    </Anchor>
                </Group>
                <Text size="sm" color="dimmed">
                    {formattedDate}
                </Text>
            </Group>
        </Card>
    );
};

export default Headline;
