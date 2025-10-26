import React from 'react';
import {Card, Text, Anchor, Group, Badge, Divider, Button} from '@mantine/core';
import {IconExternalLink} from '@tabler/icons-react';
import HeadlineData from "./headline-data.ts";
import {useStore} from "../store";

interface HeadlineProps extends HeadlineData {
    relatedArticles: (id: string) => void;
}

const Headline: React.FC<HeadlineProps> = ({id, title, link, source, crawlingDate, keywords, relatedArticles}) => {
    const formattedDate = new Date(crawlingDate).toLocaleDateString('de-DE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });

    const {selectedKeywords, toggleKeyword} = useStore();

    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            <Group position="apart" mb="md">
                <Text weight={500} size="lg" style={{flex: 1}}>{title}</Text>
                <Text size="sm" color="dimmed">
                    {formattedDate}
                </Text>
            </Group>
            <Group position="apart" mt="xl">
                <Group>
                    <Button onClick={() => relatedArticles(id)} variant="outline" size="xs">Related Articles</Button>
                </Group>
                <Group spacing="xs" align="center">
                    <Badge color="blue" variant="filled" size="lg">
                        {source}
                    </Badge>
                    <Divider orientation="vertical" style={{margin: '0 8px'}}/>
                    {keywords.map((keyword, index) => (
                        <Badge
                            key={index}
                            color={selectedKeywords.includes(keyword) ? "green" : "blue"}
                            variant="light"
                            style={{cursor: 'pointer'}}
                            onClick={() => toggleKeyword(keyword)}
                        >
                            #{keyword}
                        </Badge>
                    ))}
                    <Divider orientation="vertical" style={{margin: '0 8px'}}/>
                    <Anchor
                        href={link}
                        target="_blank"
                        rel="noopener noreferrer"
                        style={{display: 'flex', alignItems: 'center'}}
                    >
                        <IconExternalLink size={16}/>
                    </Anchor>
                </Group>
            </Group>
        </Card>
    );
};

export default Headline;